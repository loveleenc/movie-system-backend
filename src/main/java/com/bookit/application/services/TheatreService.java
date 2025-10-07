package com.bookit.application.services;

import com.bookit.application.entity.Theatre;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITheatreDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreService {
    private ITheatreDao theatreDao;
    private UserService userService;
    private ISeatDao seatDao;

    public TheatreService(ITheatreDao theatreDao, ISeatDao seatDao, UserService userService) {
        this.theatreDao = theatreDao;
        this.seatDao = seatDao;
        this.userService = userService;
    }

    public List<Theatre> getTheatres(){
        return this.theatreDao.findAll(this.userService.getCurrentUserId());
    }

    public Theatre getTheatre(Integer id) throws ResourceNotFoundException{
        return this.theatreDao.findById(id, this.userService.getCurrentUserId());
    }

    public Theatre create(Theatre theatre) {
        Long userId = this.userService.getCurrentUserId();
        theatre.setOwnerId(userId);
        Integer theatreId = this.theatreDao.create(theatre);
        this.seatDao.create(theatre.getSeats(), theatreId);
        this.seatDao.addPrices(theatre.getSeatCategoryAndPrices(), theatreId);
        return this.theatreDao.findById(theatreId, userId);
    }
}
