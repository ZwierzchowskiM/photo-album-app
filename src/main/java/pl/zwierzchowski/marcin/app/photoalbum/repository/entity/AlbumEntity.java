package pl.zwierzchowski.marcin.app.photoalbum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.CrudRepository;

@Entity
@Getter
@Setter
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String albumTitle;
    private String albumId;
    private String url;

    public AlbumEntity() {
    }
}
