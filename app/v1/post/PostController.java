package v1.post;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(PostAction.class)
public class PostController extends Controller {

    private HttpExecutionContext ec;
    private PostResourceHandler handler;

    @Inject
    public PostController(HttpExecutionContext ec, PostResourceHandler handler) {
        this.ec = ec;
        this.handler = handler;
    }

    public CompletionStage<Result> getAllArtist() {
        return handler.listAllArtist().thenApplyAsync(artists -> {
            final List<Artist> artistList = artists.collect(Collectors.toList());
            return ok(Json.toJson(artistList));
        }, ec.current());
    }

    public CompletionStage<Result> getArtistById(String id) {
        return handler.listArtistById(id).thenApplyAsync(optionalResource -> {
            return optionalResource.map(resource ->
                ok(Json.toJson(resource))
            ).orElseGet(() ->
                notFound()
            );
        }, ec.current());
    }

    public CompletionStage<Result> addArtist() {
      JsonNode json = request().body().asJson();
      final Artist artist = Json.fromJson(json, Artist.class);
      return handler.addArtist(artist).thenApplyAsync(savedResource -> {
          return created(Json.toJson(savedResource));
      }, ec.current());
    }

    public CompletionStage<Result> deleteArtistById(String id) {
      return handler.removeArtist(id).thenApplyAsync(artists -> {
          final List<Artist> artistList = artists.collect(Collectors.toList());
          return ok(Json.toJson(artistList));
      }, ec.current());
    }

    public CompletionStage<Result> getAllSongs() {
        return handler.listAllSong().thenApplyAsync(songs -> {
            final List<Song> songList = songs.collect(Collectors.toList());
            return ok(Json.toJson(songList));
        }, ec.current());
    }

    public CompletionStage<Result> getSongById(String id) {
        return handler.listSongById(id).thenApplyAsync(optionalResource -> {
            return optionalResource.map(resource ->
                ok(Json.toJson(resource))
            ).orElseGet(() ->
                notFound()
            );
        }, ec.current());
    }

    public CompletionStage<Result> addSong() {
      JsonNode json = request().body().asJson();
      final Song song = Json.fromJson(json, Song.class);
      return handler.addSong(song).thenApplyAsync(savedResource -> {
          return created(Json.toJson(savedResource));
      }, ec.current());
    }

    public CompletionStage<Result> deleteSongById(String id) {
      return handler.removeSong(id).thenApplyAsync(songs -> {
          final List<Song> songList = songs.collect(Collectors.toList());
          return ok(Json.toJson(songList));
      }, ec.current());
    }

    public CompletionStage<Result> updateSong() {
      JsonNode json = request().body().asJson();
      final SongArtist songArtist = Json.fromJson(json, SongArtist.class);
      return handler.updateSong(songArtist).thenApplyAsync(savedResource -> {
          return created(Json.toJson(savedResource));
      }, ec.current());
    }
}
