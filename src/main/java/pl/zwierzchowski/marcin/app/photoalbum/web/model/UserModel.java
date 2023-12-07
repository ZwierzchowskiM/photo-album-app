package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserModel {

    private String email;
    private String firstName;
    private String lastName;
    private List<PhotoModel> photos;

}
