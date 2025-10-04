package com.bookit.application.services;

import com.bookit.application.entity.Theatre;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITheatreDao;
import com.bookit.application.persistence.IUserDao;
import com.bookit.application.security.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreService {
    private ITheatreDao theatreDao;
    private IUserDao userDao;
    private ISeatDao seatDao;

    public TheatreService(ITheatreDao theatreDao, IUserDao userDao, ISeatDao seatDao) {
        this.theatreDao = theatreDao;
        this.userDao = userDao;
        this.seatDao = seatDao;
    }

    public List<Theatre> getTheatres(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        return this.theatreDao.findAll(user.getId());
    }

    public Theatre getTheatre(Integer id) throws ResourceNotFoundException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        return this.theatreDao.findById(id, user.getId());
    }

    public Theatre create(Theatre theatre) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        theatre.setOwnerId(user.getId());
        Integer theatreId = this.theatreDao.create(theatre);
        this.seatDao.create(theatre.getSeats(), theatreId);
        this.seatDao.addPrices(theatre.getSeatCategoryAndPrices(), theatreId);
        return this.theatreDao.findById(theatreId, user.getId());
    }
}
