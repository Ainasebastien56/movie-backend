package dev.sebastien.movies;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController  {
    @Autowired
    private MovieService movieService;

    //Get /api/v1/movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(){
        return new ResponseEntity<List<Movie>>(movieService.allMovies(), HttpStatus.OK);
    }

    //Get /api/v1/movies/imdbId
    @GetMapping("/{imdbId}")
    public ResponseEntity<Optional<Movie>> getSingleMovie(@PathVariable String imdbId){
        return new ResponseEntity<Optional<Movie>>(movieService.singleMovie(imdbId), HttpStatus.OK);
    }

    //Get /api/v1/movies/genre/Action
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genre){
        return new ResponseEntity<>(movieService.moviesByGenre(genre), HttpStatus.OK);
    }

    // Post /api/v1/movies
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        return new ResponseEntity<>(movieService.createMovie(movie), HttpStatus.CREATED);
    }

    // PUT /api/v1/movies/tt11116912
    @PutMapping("/{imdbId}")
    public ResponseEntity<?> updateMovie(@PathVariable String imdbId, @RequestBody Movie updatedData){
        Optional<Movie> result = movieService.updateMovie(imdbId, updatedData);
        if (result.isEmpty()){
            return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    // DELETE /api/v1/movies/tt11116912
    @DeleteMapping("/{imdbId}")
    public ResponseEntity<?> deleteMovie(@PathVariable String imdbId){
        boolean deleted = movieService.deleteMovie(imdbId);
        if (!deleted){
            return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
