package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.GooglePhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {


    GooglePhotoService googlePhotosService;

    public AlbumController(GooglePhotoService googlePhotosService) {
        this.googlePhotosService = googlePhotosService;
    }

    @PostMapping ("/")
    public ResponseEntity<AlbumEntity> postAlbum(@RequestParam String name) throws IOException {

        AlbumEntity createdAlbum = googlePhotosService.createAlbum(name);

        return ResponseEntity.ok(createdAlbum);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumEntity> getAlbum(@RequestParam String albumId) throws IOException {

        AlbumEntity albumEntity= googlePhotosService.getAlbum(albumId);

        return ResponseEntity.ok(albumEntity);
    }

}
