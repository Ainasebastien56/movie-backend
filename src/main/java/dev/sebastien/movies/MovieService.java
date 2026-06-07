package dev.sebastien.movies;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Movie> allMovies(){
        return movieRepository.findAll();
    }

    public Optional<Movie> singleMovie(String imdbId){
        return movieRepository.findMovieByImdbId(imdbId);
    }

    public List<Movie> moviesByGenre(String genre){
        return movieRepository.findByGenresContainingIgnoreCase(genre);
    }

    public Movie createMovie(Movie movie){
        return movieRepository.insert(movie);
    }

    public Optional<Movie> updateMovie(String imdbId, Movie updatedData){
        Optional<Movie> existing = movieRepository.findMovieByImdbId(imdbId);
        if(existing.isEmpty()) return Optional.empty();

        Query query = new Query(Criteria.where("imdbId").is(imdbId));
        Update update = new Update();

        if (updatedData.getTitle() !=null) update.set("title", updatedData.getTitle());
        if (updatedData.getTrailerLink() !=null) update.set("trailerLink", updatedData.getTrailerLink());
        if (updatedData.getGenres() !=null) update.set("genres", updatedData.getGenres());
        if (updatedData.getPoster() !=null) update.set("poster", updatedData.getPoster());
        if (updatedData.getBackdrops() !=null) update.set("backdrops", updatedData.getBackdrops());

        mongoTemplate.updateFirst(query, update, Movie.class);

        return movieRepository.findMovieByImdbId(imdbId);
    }

    public boolean deleteMovie(String imdbId){
        Optional<Movie> existing = movieRepository.findMovieByImdbId(imdbId);
        if (existing.isEmpty()) return false;

        movieRepository.delete(existing.get());
        return true;
    }
}
