package com.bookit.application.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public record ShowTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
    public ShowTimeSlot {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            //TODO: throw error
        }
    }

    public Long getSlotDuration() {
        return this.endTime.toEpochSecond(ZoneOffset.UTC) - this.startTime.toEpochSecond(ZoneOffset.UTC);
    }

    private Boolean inRange(LocalDateTime timestamp) {
        return timestamp.isAfter(this.startTime) && timestamp.isBefore(this.endTime);
    }

    public static Boolean noOverlapBetweenTimeSlotsExists(List<ShowTimeSlot> slots, ShowTimeSlot expectedTimeSlot) {
        boolean noOverlapExists = true;
        for (ShowTimeSlot slot : slots) {
            if (slot.inRange(expectedTimeSlot.startTime()) || slot.inRange(expectedTimeSlot.endTime())) {
                noOverlapExists = false;
                break;
            }
        }
        return noOverlapExists;
    }
}
