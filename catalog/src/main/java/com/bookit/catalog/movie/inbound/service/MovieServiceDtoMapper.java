package com.bookit.catalog.movie.inbound.service;

import com.bookit.catalog.movie.services.MovieException;
import com.bookit.catalog.movie.entity.Movie;
import com.bookit.catalog.movie.entity.MoviePage;
import com.bookit.catalog.movie.services.additionalInformation.MovieAdditionalInformationService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieServiceDtoMapper {
    private MovieAdditionalInformationService movieAdditionalInformationService;

    public MovieServiceDtoMapper( MovieAdditionalInformationService movieAdditionalInformationService) {
        this.movieAdditionalInformationService = movieAdditionalInformationService;
    }


    public MovieServiceDto toDTO(Movie movie) {
            return new MovieServiceDtoBuilder()
                    .setName(movie.getName())
                    .setDuration(movie.getDuration())
                    .setPoster(movie.getPoster())
                    .setGenreList(movie.getGenreList())
                    .setLanguages(movie.getLanguages())
                    .setReleaseDate(movie.getReleaseDate())
                    .setId(movie.getId())
                    .build();
    }

    public List<MovieServiceDto> toDTO(List<Movie> movies) throws MovieException {

        List<MovieServiceDto> movieServiceDtos = new ArrayList<>();

        for(Movie movie: movies) {
            MovieServiceDto movieServiceDto = this.toDTO(movie);
            String plot = this.movieAdditionalInformationService.getMoviePlot(movie.getName());
            movieServiceDto.setPlot(plot);
            movieServiceDtos.add(movieServiceDto);
        }
        return movieServiceDtos;

//        List<CompletableFuture<?>> futures = new ArrayList<>();
//        for(Movie movie: movies){
//            futures.add(CompletableFuture.supplyAsync(() -> this.movieExternalInformationService.getMoviePlot(movie.getName()))
//                    .thenAccept(plot -> {
//                        movieServiceDto.setPlot(plot);
//                        movieServiceDtos.add(movieServiceDto);
//                    })
//                    .handle((result, exception) -> {
//                        if(exception != null){
//                            logger.warn("Unable to add a plot for movie {} with id {}",
//                                    movieServiceDto.getName(), movieServiceDto.getId());//TODO: handle this correctly later
//                            movieServiceDtos.add(movieServiceDto);
//                        }
//                        return result;
//                    })
//            );
//        }
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
//                .join();


//        MovieServiceDtoMapper currentRef = this;
//        int MAX_THREADS = 5;
//        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
//        List<Future<MovieServiceDto>> movieDtoFutures = new ArrayList<>(movies.size());
//        for(Movie movie : movies){
//            movieDtoFutures.add(executorService.submit(() -> currentRef.toDTO(movie)));
//        }
//        for(int i = 0; i < movieDtoFutures.size(); i++){
//            try{
//                Future<MovieServiceDto> future = movieDtoFutures.get(i);
//                MovieServiceDto movieServiceDto = future.get(200, TimeUnit.MILLISECONDS);
//                movieServiceDtos.add(movieServiceDto);
//            }
//            catch(TimeoutException e){
//                logger.warn("Movie with id {} and name {} not added to the list due to timeout",
//                        movies.get(i).getId(),
//                        movies.get(i).getName());
//            }
//        }

    }

    public MoviePageServiceDto toDTO(MoviePage moviePage)  {
        return new MoviePageServiceDto(moviePage.pages(), this.toDTO(moviePage.movies()));
    }

    public Movie toMovie(MovieServiceDto movieDTO) throws NullPointerException{
        return new Movie(movieDTO.getName(),
                movieDTO.getDuration(),
                movieDTO.getPoster(),
                movieDTO.getGenreList(),
                movieDTO.getReleaseDate(),
                movieDTO.getLanguages(),
                null);
    }
}
