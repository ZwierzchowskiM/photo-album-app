package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.service.GooglePhotoService;

import java.io.IOException;

@RestController
@RequestMapping("/albums")
public class AlbumController {


    GooglePhotoService googlePhotosService;

    public AlbumController(GooglePhotoService googlePhotosService) {
        this.googlePhotosService = googlePhotosService;
    }

    @PostMapping ("/")
    public ResponseEntity<Void> postAlbum(@RequestParam String name) throws IOException {

        googlePhotosService.createAlbum(name);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
