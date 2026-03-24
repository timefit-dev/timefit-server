package com.example.timefit.domain.timeslot.service;

import com.example.timefit.domain.exeption.CustomException;
import com.example.timefit.domain.exeption.ErrorCode;
import com.example.timefit.domain.room.entity.Participant;
import com.example.timefit.domain.room.entity.Room;
import com.example.timefit.domain.room.repository.ParticipantRepository;
import com.example.timefit.domain.room.repository.RoomRepository;
import com.example.timefit.domain.timeslot.dto.TimeSlotDetailResponse;
import com.example.timefit.domain.timeslot.dto.TimeSlotRequest;
import com.example.timefit.domain.timeslot.dto.TimeSlotResultsResponse;
import com.example.timefit.domain.timeslot.entity.TimeSlot;
import com.example.timefit.domain.timeslot.repository.TimeSlotRepository;
import com.example.timefit.domain.user.entity.User;
import com.example.timefit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public TimeSlotResultsResponse submit(Long roomId, Long userId, TimeSlotRequest request) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Participant participant = participantRepository.findByRoom_IdAndUser_Id(roomId, userId)
                .orElseGet(() -> participantRepository.save(new Participant(room, user)));

        timeSlotRepository.deleteByParticipantId(participant.getId());

        List<LocalDateTime> availableSlots = Collections.emptyList();

        if (request.availableSlots() != null) {
            availableSlots = request.availableSlots();
        }

        List<TimeSlot> toSave = availableSlots.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(dt -> new TimeSlot(participant, dt, true))
                .toList();

        timeSlotRepository.saveAll(toSave);

        participant.markResponded();

        return getResults(roomId, userId);
    }

    @Transactional(readOnly = true)
    public TimeSlotResultsResponse getResults(Long roomId, Long userId) {

        roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        long totalParticipants = participantRepository.countByRoom_Id(roomId);
        long respondedCount = participantRepository.countByRoom_IdAndHasRespondedTrue(roomId);

        AggregationResult agg = aggregate(roomId);

        List<String> best = agg.bestSlots().stream()
                .map(LocalDateTime::toString)
                .toList();

        List<String> last = agg.lastSlots().stream()
                .map(LocalDateTime::toString)
                .toList();

        List<TimeSlotResultsResponse.SlotCount> slotCounts = agg.counts().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new TimeSlotResultsResponse.SlotCount(e.getKey().toString(), e.getValue()))
                .toList();

        return new TimeSlotResultsResponse(
                roomId,
                totalParticipants,
                respondedCount,
                best,
                last,
                slotCounts
        );
    }

    @Transactional(readOnly = true)
    public TimeSlotDetailResponse getResultDetail(Long roomId, Long userId, String dateTimeStr) {

        roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("dateTime 형식이 올바르지 않습니다. 예) 2026-02-10T13:00");
        }

        List<Participant> allParticipants = participantRepository.findByRoom_Id(roomId);

        List<TimeSlot> availableSlots = timeSlotRepository.findByParticipantRoomIdAndDateTime(roomId, dateTime);

        Set<Long> availableParticipantIds = availableSlots.stream()
                .filter(TimeSlot::isAvailable)
                .map(ts -> ts.getParticipant().getId())
                .collect(Collectors.toSet());

        List<TimeSlotDetailResponse.PersonInfo> availablePeople = allParticipants.stream()
                .filter(p -> availableParticipantIds.contains(p.getId()))
                .map(p -> new TimeSlotDetailResponse.PersonInfo(
                p.getUser().getId(),
                p.getUser().getNickname(),
                p.getUser().getProfileImageUrl()
        ))
                .toList();

        List<TimeSlotDetailResponse.PersonInfo> unavailablePeople = allParticipants.stream()
                .filter(p -> !availableParticipantIds.contains(p.getId()))
                .map(p -> new TimeSlotDetailResponse.PersonInfo(
                p.getUser().getId(),
                p.getUser().getNickname(),
                p.getUser().getProfileImageUrl()
        ))
                .toList();

        return new TimeSlotDetailResponse(
                roomId,
                dateTime.toString(),
                availablePeople,
                unavailablePeople
        );
    }

    private AggregationResult aggregate(Long roomId) {

        List<TimeSlot> roomSlots = timeSlotRepository.findByParticipantRoomId(roomId);

        Map<LocalDateTime, Set<Long>> timeToParticipantIds = new HashMap<>();

        for (TimeSlot slot : roomSlots) {
            if (!slot.isAvailable()) {
                continue;
            }

            LocalDateTime time = slot.getDateTime();
            Long participantId = slot.getParticipant().getId();

            timeToParticipantIds
                    .computeIfAbsent(time, key -> new HashSet<>())
                    .add(participantId);
        }

        if (timeToParticipantIds.isEmpty()) {
            return new AggregationResult(List.of(), List.of(), Map.of());
        }

        Map<LocalDateTime, Integer> timeToCount = timeToParticipantIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size()
                ));

        int max = timeToCount.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        List<LocalDateTime> bestSlots = timeToCount.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        int second = max - 1;

        List<LocalDateTime> lastSlots;

        if (second <= 0) {
            lastSlots = List.of();
        } else {
            lastSlots = timeToCount.entrySet().stream()
                    .filter(e -> e.getValue() == second)
                    .map(Map.Entry::getKey)
                    .sorted()
                    .toList();
        }

        return new AggregationResult(bestSlots, lastSlots, timeToCount);
    }

    private record AggregationResult(
            List<LocalDateTime> bestSlots,
            List<LocalDateTime> lastSlots,
            Map<LocalDateTime, Integer> counts
            ) {

    }
}
