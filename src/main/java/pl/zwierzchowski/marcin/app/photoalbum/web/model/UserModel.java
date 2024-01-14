package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserModel {

    @Email(message = "email is mandatory")
    private String email;
    @NotBlank(message = "first name is mandatory")
    private String firstName;
    @NotBlank(message = "last name is mandatory")
    private String lastName;

}
