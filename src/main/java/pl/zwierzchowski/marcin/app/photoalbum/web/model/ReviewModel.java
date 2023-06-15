package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import lombok.Getter;
import lombok.Setter;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;


@Getter
@Setter
public class ReviewModel {

    private Long photoId;
    private Result result;
    private String comment;

}
