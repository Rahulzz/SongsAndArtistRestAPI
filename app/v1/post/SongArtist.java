package v1.post;

public class SongArtist {
    private Long songId;
    private Long artistId;

    public SongArtist() {
    }

    public SongArtist(Long songId, Long artistId) {
        this.songId = songId;
        this.artistId = artistId;
    }

    public Long getSongId() {
        return songId;
    }

    public Long getArtistId() {
        return artistId;
    }
}
