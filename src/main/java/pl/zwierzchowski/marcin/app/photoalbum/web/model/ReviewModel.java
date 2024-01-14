package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;


@Getter
@Setter
public class ReviewModel {

    @NotNull(message = "PhotoId is mandatory")
    private Long photoId;
    @NotNull(message = "Result of review is mandatory")
    private Result result;
    @NotBlank(message = "Comment is mandatory")
    private String comment;
    private String album;

}
