package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zwierzchowski.marcin.app.photoalbum.service.GooglePhotosService;

import java.io.IOException;

@RestController
@RequestMapping("/albums")
public class AlbumController {


    GooglePhotosService googlePhotosService;

    public AlbumController(GooglePhotosService googlePhotosService) {
        this.googlePhotosService = googlePhotosService;
    }


    @GetMapping("/")
    public ResponseEntity<Void> getPhoto() throws IOException {

        googlePhotosService.testAlbum();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
