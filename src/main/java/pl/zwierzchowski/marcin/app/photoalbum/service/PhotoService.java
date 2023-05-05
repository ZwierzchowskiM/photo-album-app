package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Optional<PhotoEntity> getPhoto(Long id) {

        return photoRepository.findById(id);
    }

    public PhotoEntity save (String S3address, String name){

        PhotoEntity photo = new PhotoEntity();
        photo.setSubmittedDate(LocalDateTime.now());
        photo.setDescription(name);
        photo.setFileUniqueName(S3address);

        return photoRepository.save(photo);

    }
}
