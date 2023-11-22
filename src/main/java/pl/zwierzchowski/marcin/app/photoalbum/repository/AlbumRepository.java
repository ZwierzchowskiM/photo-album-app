package pl.zwierzchowski.marcin.app.photoalbum.repository;

import org.springframework.data.repository.CrudRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

public interface AlbumRepository extends CrudRepository<AlbumEntity, Long> {
}
