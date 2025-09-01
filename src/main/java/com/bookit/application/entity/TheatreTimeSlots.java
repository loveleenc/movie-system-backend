package com.bookit.application.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public record TheatreTimeSlots(LocalDateTime startTime, LocalDateTime endTime) {
    public TheatreTimeSlots {
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

    public static Boolean noOverlapBetweenTimeSlotsExists(List<TheatreTimeSlots> slots, TheatreTimeSlots expectedTimeSlot) {
        boolean noOverlapExists = true;
        for (TheatreTimeSlots slot : slots) {
            if (slot.inRange(expectedTimeSlot.startTime()) || slot.inRange(expectedTimeSlot.endTime())) {
                noOverlapExists = false;
                break;
            }
        }
        return noOverlapExists;
    }
}
