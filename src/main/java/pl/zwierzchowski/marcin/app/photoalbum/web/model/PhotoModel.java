package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;

import java.time.LocalDateTime;

public class PhotoModel {

    private String fileName;
    private String description;
    private String objectKey;
    private UserModel user;
    private LocalDateTime submittedDate;
    private Status status;
    private ReviewModel reviewResult;

}
