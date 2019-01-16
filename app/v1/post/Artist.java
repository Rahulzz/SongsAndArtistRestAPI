package v1.post;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "artist")
public class Artist {

    public Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String name;
}
