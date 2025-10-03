package com.bookit.application.utils;

import com.bookit.application.dto.seat.TheatreRowDto;
import com.bookit.application.dto.theatre.TheatreDto;
import com.bookit.application.entity.Seat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TheatreUtil {
    static boolean rowLettersMatchRequiredCriteria(List<TheatreRowDto> theatreRowDtos){
        List<String> rowLetters = theatreRowDtos.stream().map(TheatreRowDto::getRowLetter).distinct().toList();
        if(!Objects.deepEquals(theatreRowDtos.size(), rowLetters.size())){
            return false;
        }
        for(String rowLetter: rowLetters){
            if(Objects.isNull(rowLetter) || rowLetter.length() != 1 || !Character.isAlphabetic(rowLetter.charAt(0))){
                return false;
            }
        }
        return true;
    }

    public static List<Seat> createSeats(TheatreDto theatreDto){
        List<Seat> seats = new ArrayList<>();
        if(!TheatreUtil.rowLettersMatchRequiredCriteria(theatreDto.getRowDetailsDto())){
            throw new IllegalArgumentException("Row letters provided as input do the match the required criteria.");
        }
        for(TheatreRowDto row: theatreDto.getRowDetailsDto()){
            for(int i = 1; i <= row.getSeatCount(); i++){
                String seatNumber = String.format("%s%s", row.getRowLetter(), i);
                Seat seat = new Seat(seatNumber, row.getSeatType(), row.getSeatPrice());
                seats.add(seat);
            }
        }
        return seats;
    }
}
