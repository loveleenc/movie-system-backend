//package com.bookit.security.user.booking.local;
//
//import com.bookit.application.booking.db.ICartDao;
//import com.bookit.security.user.booking.BookingClient;
//import com.bookit.security.user.comms.Request;
//import com.bookit.security.user.comms.Response;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.Component;
//
//@Component("securityUserLocalClient")
//public class BookingLocalClient implements BookingClient {
//    private ICartDao cartDao;
//
//    public BookingLocalClient(ICartDao cartDao) {
//        this.cartDao = cartDao;
//    }
//
//    @Override
//    public void sendRequest(Request request) {
//        throw new RuntimeException("Not implemented yet");
//    }
//
//    @Override
//    public Object processResponse(Response response) {
//        throw new RuntimeException("Not implemented yet");
//    }
//
//    @Override
//    public void createCart(Long userId) {
//        this.cartDao.createCart(userId);
//    }
//}
