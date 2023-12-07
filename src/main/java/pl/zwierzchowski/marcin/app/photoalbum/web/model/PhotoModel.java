package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import lombok.Getter;
import lombok.Setter;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class PhotoModel {

    private String fileName;
    private String description;
    private String objectKey;
    private String userEmail;
    private LocalDateTime submittedDate;
    private Status status;
    private Result reviewResult;
    private String comment;

}
