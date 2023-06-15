package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.PhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.service.S3Service;

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


}
