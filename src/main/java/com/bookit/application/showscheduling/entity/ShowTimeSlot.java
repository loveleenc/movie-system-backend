package com.bookit.application.showscheduling.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public record ShowTimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
    public ShowTimeSlot {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Show cannot start after or at the same time as end time");
        }
    }

    public Long getSlotDuration() {
        return (this.endTime.toEpochSecond(ZoneOffset.UTC) - this.startTime.toEpochSecond(ZoneOffset.UTC))/60;
    }

    private Boolean inRange(LocalDateTime timestamp) {
        return timestamp.isAfter(this.startTime) && timestamp.isBefore(this.endTime);
    }

    public static Boolean noOverlapBetweenTimeSlotsExists(List<ShowTimeSlot> slots, ShowTimeSlot expectedTimeSlot) {
        boolean noOverlapExists = true;
        for (ShowTimeSlot slot : slots) {
            if ((slot.inRange(expectedTimeSlot.startTime()) || slot.inRange(expectedTimeSlot.endTime())) ||
                    (expectedTimeSlot.startTime.isEqual(slot.startTime) && expectedTimeSlot.endTime.isEqual(slot.endTime))
            ) {
                noOverlapExists = false;
                break;
            }
        }
        return noOverlapExists;
    }
}
