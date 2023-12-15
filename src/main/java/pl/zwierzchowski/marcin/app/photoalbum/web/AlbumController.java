package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.service.GPhotosAlbumService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    GPhotosAlbumService gPhotosService;

    public AlbumController(GPhotosAlbumService gPhotosService) {
        this.gPhotosService = gPhotosService;
    }

    @PostMapping("/")
    public ResponseEntity<AlbumModel> postAlbum(@RequestParam String name) throws IOException {

        AlbumModel createdAlbum = gPhotosService.createAlbum(name);

        return ResponseEntity.ok(createdAlbum);
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumModel> getAlbum(@RequestParam String albumId) throws IOException {

        AlbumModel albumModel = null;
        try {
            albumModel = gPhotosService.getAlbum(albumId);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(albumModel);
    }

    @GetMapping("/")
    public ResponseEntity<List<AlbumModel>> getAlbums() throws IOException {

        List<AlbumModel> albums;
        albums = gPhotosService.getAlbums();

        return ResponseEntity.ok(albums);
    }
}
