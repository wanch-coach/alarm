package com.wanchcoach.alarm.domain.service.dto;

public record DrugAlarm(
        Long familyId,
        String name,
        String deviceToken
) {
}
