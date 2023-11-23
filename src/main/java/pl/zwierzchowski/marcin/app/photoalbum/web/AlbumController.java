package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.service.AlbumService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;

import java.io.IOException;

@RestController
@RequestMapping("/albums")
public class AlbumController {


    AlbumService googlePhotosService;


    public AlbumController(AlbumService googlePhotosService) {
        this.googlePhotosService = googlePhotosService;
    }

    @PostMapping ("/")
    public ResponseEntity<AlbumModel> postAlbum(@RequestParam String name) throws IOException {

        AlbumModel createdAlbum = googlePhotosService.createAlbum(name);

        return ResponseEntity.ok(createdAlbum);
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumModel> getAlbum(@RequestParam String albumId) throws IOException {

        AlbumModel albumModel= googlePhotosService.getAlbum(albumId);

        return ResponseEntity.ok(albumModel);
    }



}
