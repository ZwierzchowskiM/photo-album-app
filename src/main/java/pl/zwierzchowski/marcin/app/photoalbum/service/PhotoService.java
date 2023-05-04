package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

import java.util.Optional;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Optional<PhotoEntity> getImage(Long id) {

        return photoRepository.findById(id);

    }
}
