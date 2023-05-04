package pl.zwierzchowski.marcin.app.photoalbum.repository.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String description;
    private String fileUniqueName;
    @ManyToOne
    private UserEntity user;
    @ManyToOne
    private UserEntity reviewer;
    private LocalDateTime submittedDate;

    public PhotoEntity() {
    }


}
