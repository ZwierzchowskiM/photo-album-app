package pl.zwierzchowski.marcin.app.photoalbum.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public
class MethodArgumentNotValidException extends RuntimeException {
    public MethodArgumentNotValidException(String s) {
        super(s);
    }
}