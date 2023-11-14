package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.PhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.service.S3Service;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    S3Service s3Service;
    PhotoService photoService;

    public PhotoController(S3Service s3Service, PhotoService photoService) {
        this.s3Service = s3Service;
        this.photoService = photoService;
    }

//    @PostMapping("/upload")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PhotoEntity uploadPhoto(@RequestParam("file") MultipartFile file, String description) {

        PhotoEntity savedPhoto = photoService.save(file,description);
        System.out.println("You have placed file into the S3 bucket");

        return savedPhoto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoEntity> getPhoto(@PathVariable Long id) {

        PhotoEntity photo = photoService.findPhotoById(id);

        return ResponseEntity.ok(photo);
    }


    @GetMapping( "/download/{id}")
    public ResponseEntity<byte[]> imageDownload(@PathVariable Long id) throws IOException {

        ResponseEntity<byte[]> byteStream = photoService.downloadPhoto(id);

        return byteStream;
    }


    @DeleteMapping("/{id}")
    ResponseEntity<String> deletePhoto(@PathVariable Long id) {

        photoService.deletePhoto(id);
        return ResponseEntity.ok("Review deleted");
    }
}
//    @GetMapping("/download/{id}")
//    public ResponseEntity<PhotoEntity> downloadPhoto(@PathVariable Long id) {
//
//        PhotoEntity photo = photoService.findPhotoById(id);
//
//        if (photo == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        String photoPath = photo.getObjectKey();
//        File downloadFile = new File(photoPath);
//        InputStreamResource resource = null;
//        try {
//            resource = new InputStreamResource(new FileInputStream(downloadFile));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return ResponseEntity.notFound().build();
//        }
//
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFile.getName());
//
//
//
//        return ResponseEntity.ok()
//                .headers(header)
//                .contentLength(downloadFile.length())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(resource);
//
//    }
