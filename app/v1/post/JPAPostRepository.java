package v1.post;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that provides a non-blocking API with a custom execution context
 * and circuit breaker.
 */
@Singleton
public class JPAPostRepository implements PostRepository {

    private final JPAApi jpaApi;
    private final PostExecutionContext ec;
    private final CircuitBreaker circuitBreaker = new CircuitBreaker().withFailureThreshold(1).withSuccessThreshold(3);

    @Inject
    public JPAPostRepository(JPAApi api, PostExecutionContext ec) {
        this.jpaApi = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Stream<Artist>> listAllArtist() {
        return supplyAsync(() -> wrap(em -> getAllArtist(em)), ec);
    }

    @Override
    public CompletionStage<Optional<Artist>> getArtistById(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookupArtist(em, id))), ec);
    }

    @Override
    public CompletionStage<Artist> addArtist(Artist artist) {
        return supplyAsync(() -> wrap(em -> insertArtist(em, artist)), ec);
    }

    @Override
    public CompletionStage<Stream<Artist>> removeArtist(Artist artist) {
        return supplyAsync(() -> wrap(em -> deleteArtist(em, artist)), ec);
    }

    private Stream<Artist> getAllArtist(EntityManager em) {
        TypedQuery<Artist> query = em.createQuery("SELECT p FROM Artist p", Artist.class);
        return query.getResultList().stream();
    }

    private Artist insertArtist(EntityManager em, Artist artist) {
        return em.merge(artist);
    }

    private Stream<Artist> deleteArtist(EntityManager em, Artist artist) {
        artist = em.find(Artist.class, artist.id);

        if(artist != null){
            em.remove(artist);
        }

        TypedQuery<Artist> query = em.createQuery("SELECT p FROM Artist p", Artist.class);
        return query.getResultList().stream();
    }

    private Optional<Artist> lookupArtist(EntityManager em, Long id) throws SQLException {
        TypedQuery<Artist> query = em.createQuery("SELECT p FROM Artist p where p.id = :id", Artist.class);
        query.setParameter("id", id);
        return query.setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Override
    public CompletionStage<Stream<Song>> listAllSong() {
        return supplyAsync(() -> wrap(em -> getAllSong(em)), ec);
    }

    @Override
    public CompletionStage<Optional<Song>> getSongById(Long id) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> lookupSong(em, id))), ec);
    }

    @Override
    public CompletionStage<Song> addSong(Song song) {
        return supplyAsync(() -> wrap(em -> insertSong(em, song)), ec);
    }

    @Override
    public CompletionStage<Stream<Song>> removeSong(Song song) {
        return supplyAsync(() -> wrap(em -> deleteSong(em, song)), ec);
    }

    private Stream<Song> getAllSong(EntityManager em) {
        TypedQuery<Song> query = em.createQuery("SELECT p FROM Song p", Song.class);
        return query.getResultList().stream();
    }

    private Optional<Song> lookupSong(EntityManager em, Long id) throws SQLException {
        TypedQuery<Song> query = em.createQuery("SELECT p FROM Song p where p.id = :id", Song.class);
        query.setParameter("id", id);
        return query.setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    private Song insertSong(EntityManager em, Song song) {
        return em.merge(song);
    }

    private Stream<Song> deleteSong(EntityManager em, Song song) {
        song = em.find(Song.class, song.id);

        if(song != null){
            em.remove(song);
        }

        TypedQuery<Song> query = em.createQuery("SELECT p FROM Song p", Song.class);
        return query.getResultList().stream();
    }

    @Override
    public CompletionStage<Optional<Song>> updateSong(Long songId, Long artistId) {
        return supplyAsync(() -> wrap(em -> Failsafe.with(circuitBreaker).get(() -> mapSongToArtist(em, songId, artistId))), ec);
    }

    private Optional<Song> mapSongToArtist(EntityManager em, Long songId, Long artistId) throws InterruptedException {
        final Song song = em.find(Song.class, songId);
        if (song != null) {
            song.artist = artistId;
        }
        return Optional.ofNullable(song);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }
}
