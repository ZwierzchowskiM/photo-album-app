package pl.zwierzchowski.marcin.app.photoalbum.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> exception(ResourceNotFoundException exception) {
        String msg  = exception.getMessage();
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> exception(MethodArgumentNotValidException exception) {

        List<String> errors = new ArrayList<>();
        exception.getAllErrors().forEach(error -> errors.add((error.getDefaultMessage())));

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
