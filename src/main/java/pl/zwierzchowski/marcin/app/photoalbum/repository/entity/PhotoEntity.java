package pl.zwierzchowski.marcin.app.photoalbum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
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
    private Status status;

    public PhotoEntity() {
    }

}
