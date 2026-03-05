package com.example.timefit.domain.timeslot.dto;

import java.util.List;

public record TimeSlotDetailResponse (
  Long roomId,
  String dateTime,
  List<PersonInfo> availablePeople,
  List<PersonInfo> unavailablePeople
) {
  public record PersonInfo(
    Long userId,
    String nickname,
    String profileImageUrl
  ) {}
}
