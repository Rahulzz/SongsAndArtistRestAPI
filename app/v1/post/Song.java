package v1.post;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "song")
public class Song {

    public Song() {
    }

    public Song(String title, Long artist, String imagePath, Boolean available, Long songxCount) {
        this.title = title;
        this.artist = artist;
        this.imagePath = imagePath;
        this.available = available;
        this.songxCount = songxCount;
    }

    public Song(Long id, Long artist) {
        this.id = id;
        this.artist = artist;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String title;
    public Long artist;
    public String imagePath;
    public Boolean available;
    public Long songxCount;
}
