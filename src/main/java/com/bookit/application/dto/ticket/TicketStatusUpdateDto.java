package com.bookit.application.dto.ticket;

import org.springframework.lang.NonNull;

public class TicketStatusUpdateDto {
    @NonNull
    private String showId;
    @NonNull
    private String status;

    public TicketStatusUpdateDto(@NonNull String showId, @NonNull String status) {
        this.showId = showId;
        this.status = status;
    }

    @NonNull
    public String getShowId() {
        return showId;
    }

    @NonNull
    public String getStatus() {
        return status;
    }
}
