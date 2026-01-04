package com.bookit.application.theatremanagement;

import com.bookit.application.common.ResourceNotFoundException;
import com.bookit.application.theatremanagement.dto.TheatreDto;
import com.bookit.application.theatremanagement.dto.TheatreDtoMapper;
import com.bookit.application.theatremanagement.entity.Theatre;
import com.bookit.application.theatremanagement.db.ISeatDao;
import com.bookit.application.theatremanagement.db.ITheatreDao;
import com.bookit.application.theatremanagement.user.UserGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TheatreService {
    private ITheatreDao theatreDao;
    private UserGateway userGateway;
    private ISeatDao seatDao;
    private TheatreDtoMapper theatreDtoMapper;

    public TheatreService(ITheatreDao theatreDao, ISeatDao seatDao, UserGateway userGateway, TheatreDtoMapper theatreDtoMapper) {
        this.theatreDao = theatreDao;
        this.seatDao = seatDao;
        this.userGateway = userGateway;
        this.theatreDtoMapper = theatreDtoMapper;
    }

    public List<TheatreDto> getTheatres(){
        List<Theatre> theatres = this.theatreDao.findAll(this.userGateway.getCurrentUserId());
        return this.theatreDtoMapper.toTheatreDto(theatres);
    }

    public TheatreDto getTheatre(Integer id) throws ResourceNotFoundException {
      Theatre theatre = this.theatreDao.findById(id, this.userGateway.getCurrentUserId());
      return this.theatreDtoMapper.toTheatreDto(theatre);
    }

    public TheatreDto create(TheatreDto theatreDto) {
        Theatre theatre = this.theatreDtoMapper.toTheatre(theatreDto);
        Long userId = this.userGateway.getCurrentUserId();
        theatre.setOwnerId(userId);
        Integer theatreId = this.theatreDao.create(theatre);
        this.seatDao.create(theatre.getSeats(), theatreId);
        this.seatDao.addPrices(theatre.getSeatCategoryAndPrices(), theatreId);
        Theatre createdTheatre = this.theatreDao.findById(theatreId, userId);
        return this.theatreDtoMapper.toTheatreDto(createdTheatre);

    }
}
