package v1.post;

import com.palominolabs.http.url.UrlBuilder;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;

import javax.inject.Inject;
import java.nio.charset.CharacterCodingException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Handles presentation of Post resources, which map to JSON.
 */
public class PostResourceHandler {

    private final PostRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public PostResourceHandler(PostRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }
    
    public CompletionStage<Stream<Artist>> listAllArtist() {
        return repository.listAllArtist();
    }

    public CompletionStage<Optional<Artist>> listArtistById(String id) {
        return repository.getArtistById(Long.parseLong(id));
    }

    public CompletionStage<Artist> addArtist(Artist artist) {
        return repository.addArtist(artist);
    }

    public CompletionStage<Stream<Artist>> removeArtist(String id) {
        Artist artist = new Artist();
        artist.id = Long.parseLong(id);
        return repository.removeArtist(artist);
    }

    public CompletionStage<Stream<Song>> listAllSong() {
        return repository.listAllSong();
    }

    public CompletionStage<Optional<Song>> listSongById(String id) {
        return repository.getSongById(Long.parseLong(id));
    }

    public CompletionStage<Song> addSong(Song song) {
        return repository.addSong(song);
    }

    public CompletionStage<Stream<Song>> removeSong(String id) {
        Song song = new Song();
        song.id = Long.parseLong(id);
        return repository.removeSong(song);
    }

    public CompletionStage<Optional<Song>> updateSong(SongArtist songArtist) {
        return repository.updateSong(songArtist.getSongId(), songArtist.getArtistId());
    }
}
