package com.bookit.application.dto.show;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShowAndTicketCreationDto {
    @JsonProperty("show")
    @NonNull
    private ShowDto showDto;
    private Long moviePrice;
    private String status;

    public ShowAndTicketCreationDto(@NonNull ShowDto showDto) {
        this.showDto = showDto;
    }

    public ShowAndTicketCreationDto(){}

    public ShowAndTicketCreationDto(@NonNull ShowDto showDto, Long moviePrice, String status) {
        this.showDto = showDto;
        this.moviePrice = moviePrice;
        this.status = status;
    }

    @NonNull
    public ShowDto getShowDto() {
        return showDto;
    }

    public Long getMoviePrice() {
        return moviePrice;
    }

    public String getStatus() {
        return status;
    }
}
