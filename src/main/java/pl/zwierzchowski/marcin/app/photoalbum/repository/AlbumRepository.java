package pl.zwierzchowski.marcin.app.photoalbum.repository;

import org.springframework.data.repository.CrudRepository;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends CrudRepository<AlbumEntity, Long> {

    Optional<AlbumEntity> findByAlbumId(String albumId);
}
