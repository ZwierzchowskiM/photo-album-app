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
    private String objectKey;
    @ManyToOne
    private UserEntity user;
    private LocalDateTime submittedDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @OneToOne
    private ReviewEntity reviewResult;

    public PhotoEntity() {
    }

}
