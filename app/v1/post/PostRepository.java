package v1.post;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface PostRepository {
  
    CompletionStage<Stream<Artist>> listAllArtist();

    CompletionStage<Optional<Artist>> getArtistById(Long id);

    CompletionStage<Artist> addArtist(Artist artist);

    CompletionStage<Stream<Artist>> removeArtist(Artist artist);

    CompletionStage<Stream<Song>> listAllSong();

    CompletionStage<Optional<Song>> getSongById(Long id);

    CompletionStage<Song> addSong(Song song);

    CompletionStage<Stream<Song>> removeSong(Song song);

    CompletionStage<Optional<Song>> updateSong(Long songId, Long artistId);
}
