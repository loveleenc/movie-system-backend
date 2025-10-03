package com.bookit.application.services;

import com.bookit.application.entity.Theatre;
import com.bookit.application.persistence.ISeatDao;
import com.bookit.application.persistence.ITheatreDao;
import com.bookit.application.persistence.IUserDao;
import com.bookit.application.persistence.jdbcDao.TheatreDao;
import com.bookit.application.security.entity.User;
import com.bookit.application.types.Role;
import org.springframework.security.access.AccessDeniedException;
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
        if(!user.getRoles().contains(Role.THEATRE_OWNER)){
            throw new ResourceNotFoundException("Theatres are being fetched by users that are not theatre owners");
        }
        Long userId = user.getId();
        return this.theatreDao.findAll(userId);
    }

    public Theatre getTheatre(Integer id) throws ResourceNotFoundException{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        if(!user.getRoles().contains(Role.THEATRE_OWNER)){
            throw new ResourceNotFoundException("Theatres are being fetched by users that are not theatre owners");
        }
        Long userId = user.getId();
        return this.theatreDao.findById(id, userId);
    }

    public Theatre create(Theatre theatre) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = this.userDao.findUserByUsername(username);
        if(!user.getRoles().contains(Role.THEATRE_OWNER)){ //TODO: remove/handle this in security filterChain instead. look for similar requests
            throw new AccessDeniedException("Theatres cannot be created by users that are not theatre owners");
        }
        theatre.setOwnerId(user.getId());
        Integer theatreId = this.theatreDao.create(theatre);
        this.seatDao.create(theatre.getSeats(), theatreId);
        return this.theatreDao.findById(theatreId, user.getId());
    }
}
