package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.service.GooglePhotosAlbumService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    GooglePhotosAlbumService gPhotosService;

    public AlbumController(GooglePhotosAlbumService gPhotosService) {
        this.gPhotosService = gPhotosService;
    }

    @PostMapping("/")
    public ResponseEntity<AlbumModel> postAlbum(@RequestParam String name) throws IOException {

        AlbumModel createdAlbum = gPhotosService.createAlbum(name);

        return ResponseEntity.ok(createdAlbum);
    }


    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumModel> getAlbum(@PathVariable String albumId) throws IOException {

        AlbumModel albumModel = new AlbumModel();
        albumModel = gPhotosService.getAlbum(albumId);

        return ResponseEntity.ok(albumModel);
    }

    @GetMapping("/")
    public ResponseEntity<List<AlbumModel>> getAlbums() throws IOException {

        List<AlbumModel> albums;
        albums = gPhotosService.getAlbums();

        return ResponseEntity.ok(albums);
    }
}
