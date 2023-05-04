package pl.zwierzchowski.marcin.app.photoalbum.repository;

import org.springframework.data.repository.CrudRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

public interface PhotoRepository extends CrudRepository<PhotoEntity, Long> {

}
