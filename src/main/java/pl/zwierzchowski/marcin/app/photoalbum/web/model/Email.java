package pl.zwierzchowski.marcin.app.photoalbum.web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Email {

    String to;
    String from;
    String subject;
    String text;
    String template;
    Map<String, Object> properties;
}
