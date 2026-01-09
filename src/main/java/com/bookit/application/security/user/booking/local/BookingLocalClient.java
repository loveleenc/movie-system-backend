package com.bookit.application.security.user.booking.local;

import com.bookit.application.booking.CartService;
import com.bookit.application.security.user.booking.BookingClient;
import com.bookit.application.security.user.comms.Request;
import com.bookit.application.security.user.comms.Response;


public class BookingLocalClient implements BookingClient {
    private CartService cartService;

    public BookingLocalClient(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void sendRequest(Request request) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Object processResponse(Response response) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void createCart(Long userId) {
        this.cartService.createCart(userId);
    }
}
