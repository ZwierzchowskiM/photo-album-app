package pl.zwierzchowski.marcin.app.photoalbum.repository.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private PhotoEntity photoEntity;
    @ManyToOne
    private UserEntity userCreatedBy;
    private ZonedDateTime createdDate;
    private String text;
    private String result;

}
