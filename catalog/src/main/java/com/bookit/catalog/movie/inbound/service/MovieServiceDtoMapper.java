package com.bookit.catalog.movie.inbound.service;

import com.bookit.catalog.movie.MovieException;
import com.bookit.catalog.movie.MovieExternalInformationService;
import com.bookit.catalog.movie.entity.Movie;
import com.bookit.catalog.movie.entity.MoviePage;
import com.bookit.catalog.movie.storage.StorageException;
import com.bookit.catalog.movie.storage.StorageService;
import com.bookit.catalog.movie.storage.resource.PosterResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class MovieServiceDtoMapper {
    private final Logger logger;
    private final StorageService storageService;
    private MovieExternalInformationService movieExternalInformationService;

    public MovieServiceDtoMapper(StorageService storageService, MovieExternalInformationService movieExternalInformationService) {
        this.storageService = storageService;
        this.logger = LoggerFactory.getLogger(MovieServiceDtoMapper.class);
        this.movieExternalInformationService = movieExternalInformationService;
    }


    public MovieServiceDto toDTO(Movie movie) throws MovieException {
        try {
            PosterResource resource = this.storageService.getResource(movie.getPoster());
            return new MovieServiceDtoBuilder()
                    .setName(movie.getName())
                    .setDuration(movie.getDuration())
                    .setPoster(resource.getContentOrUrlAsString())
                    .setGenreList(movie.getGenreList())
                    .setLanguages(movie.getLanguages())
                    .setReleaseDate(movie.getReleaseDate())
                    .setId(movie.getId())
                    .build();
        } catch (StorageException e) {
            throw new MovieException("Unable to fetch the movie", e);
        }
    }

    public MovieServiceDto toDTOWithoutPosterAndPlot(Movie movie)  {
            return new MovieServiceDtoBuilder()
                    .setName(movie.getName())
                    .setDuration(movie.getDuration())
                    .setGenreList(movie.getGenreList())
                    .setLanguages(movie.getLanguages())
                    .setReleaseDate(movie.getReleaseDate())
                    .setId(movie.getId())
                    .build();
    }


    public List<MovieServiceDto> toDTO(List<Movie> movies) throws MovieException, ExecutionException, InterruptedException {

        List<CompletableFuture<?>> futures = new ArrayList<>();
        List<MovieServiceDto> movieServiceDtos = new ArrayList<>();
        for(Movie movie: movies){
            MovieServiceDto movieServiceDto = this.toDTOWithoutPosterAndPlot(movie);
            futures.add(CompletableFuture.supplyAsync(() -> this.storageService.getResource(movie.getPoster()))
                    .thenAccept(resource -> movieServiceDto.setPoster(resource.getContentOrUrlAsString()))
                    .handle(((result, exception) -> {
                        logger.warn(exception.getMessage()); //TODO: handle this correctly later
                        return result;
                    })));
            futures.add(CompletableFuture.supplyAsync(() -> this.movieExternalInformationService.getMoviePlot(movie.getName()))
                    .thenAccept(plot -> {
                        movieServiceDto.setPlot(plot);
                        movieServiceDtos.add(movieServiceDto);
                    })
                    .handle((result, exception) -> {
                        logger.warn("Unable to add a plot for movie {} with id {}",
                                movieServiceDto.getName(), movieServiceDto.getId());//TODO: handle this correctly later
                        movieServiceDtos.add(movieServiceDto);
                        return result;
                    })
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .join();


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
        return movieServiceDtos;
    }

    public MoviePageServiceDto toDTO(MoviePage moviePage) throws ExecutionException, InterruptedException {
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
