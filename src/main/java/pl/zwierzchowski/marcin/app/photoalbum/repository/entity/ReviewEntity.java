package pl.zwierzchowski.marcin.app.photoalbum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private PhotoEntity photoEntity;
    @ManyToOne
    private UserEntity reviewer;
    private ZonedDateTime createdDate;
    @Enumerated
    private Result result;
    private String comment;

}
