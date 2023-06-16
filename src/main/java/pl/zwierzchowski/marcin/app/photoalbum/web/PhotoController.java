package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.PhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.service.S3Service;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    S3Service s3Service;
    PhotoService photoService;

    public PhotoController(S3Service s3Service, PhotoService photoService) {
        this.s3Service = s3Service;
        this.photoService = photoService;
    }

    @PostMapping("/upload")
    public PhotoEntity uploadPhoto(@RequestParam("file") MultipartFile file, String description) {

        PhotoEntity savedPhoto = photoService.save(file,description);
        System.out.println("You have placed file into the S3 bucket");

        return savedPhoto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoModel> getPhoto(@PathVariable Long id) {

        PhotoModel photo = photoService.findPhotoById(id);

        return ResponseEntity.ok(photo);
    }


}
