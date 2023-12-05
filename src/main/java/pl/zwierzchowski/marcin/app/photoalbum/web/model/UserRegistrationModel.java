package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationModel {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

}
