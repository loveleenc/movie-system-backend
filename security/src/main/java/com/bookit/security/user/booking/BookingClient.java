package com.bookit.security.user.booking;

import com.bookit.security.user.comms.Client;

public interface BookingClient extends Client {
    void createCart(Long userId);
}
