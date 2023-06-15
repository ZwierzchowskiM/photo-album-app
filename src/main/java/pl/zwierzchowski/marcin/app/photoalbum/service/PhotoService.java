package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;
    S3Service s3Service;

    public PhotoService(PhotoRepository photoRepository, S3Service s3Service) {
        this.photoRepository = photoRepository;
        this.s3Service = s3Service;
    }

    public Optional<PhotoEntity> getPhoto(Long id) {

        return photoRepository.findById(id);
    }

    public PhotoEntity save (MultipartFile file, String description){

        String S3address = "";
        S3address = s3Service.putObject(file);

        PhotoEntity photo = new PhotoEntity();
        photo.setSubmittedDate(LocalDateTime.now());
        photo.setDescription(description);
        photo.setFileUniqueName(S3address);
        photo.setStatus(Status.SUBMITTED);

        return photoRepository.save(photo);

    }
}
