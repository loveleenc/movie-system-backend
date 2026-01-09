package com.bookit.application.booking.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cart {
    private Long cartId;
    private LocalDateTime expiry;

    public Cart(Long cartId, LocalDateTime expiry) {
        this.cartId = cartId;
        this.expiry = expiry;
    }

    public void extendExpiry(){
        this.expiry = LocalDateTime.now().plusMinutes(30);
    }

    public Long getCartId() {
        return cartId;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }
}
