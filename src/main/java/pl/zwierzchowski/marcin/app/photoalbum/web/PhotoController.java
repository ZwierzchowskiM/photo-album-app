package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
import pl.zwierzchowski.marcin.app.photoalbum.service.PhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.service.S3Service;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    S3Service s3Service;
    PhotoService photoService;

    public PhotoController(S3Service s3Service, PhotoService photoService) {
        this.s3Service = s3Service;
        this.photoService = photoService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoModel> uploadPhoto(@RequestParam("file") MultipartFile file, String description,String userEmail) {

        PhotoModel savedPhoto = photoService.upload(file,description,userEmail);
        System.out.println("You have placed file into the S3 bucket");

        return ResponseEntity.ok(savedPhoto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoModel> getPhoto(@PathVariable Long id) {

        PhotoModel photo = photoService.findPhotoById(id);

        return ResponseEntity.ok(photo);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PhotoModel>> getUserPhotos(@PathVariable Long userId) {

        List<PhotoModel> pendingPhotos = photoService.findPhotosByUser(userId);
        return ResponseEntity.ok(pendingPhotos);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PhotoModel>> getPendingPhotos() {

        List<PhotoModel> pendingPhotos = photoService.findPendingPhotos();
        return ResponseEntity.ok(pendingPhotos);
    }

    @GetMapping( "/download/{id}")
    public ResponseEntity<byte[]> imageDownload(@PathVariable Long id) throws IOException {

        ResponseEntity<byte[]> byteStream = photoService.downloadPhoto(id);

        return byteStream;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deletePhoto(@PathVariable Long id) {

        photoService.deletePhoto(id);
        return ResponseEntity.ok("Photo deleted");
    }
}


