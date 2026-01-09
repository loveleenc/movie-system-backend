package com.bookit.application.security.user.booking;

import com.bookit.application.security.user.comms.Client;

public interface BookingClient extends Client {
    void createCart(Long userId);
}
