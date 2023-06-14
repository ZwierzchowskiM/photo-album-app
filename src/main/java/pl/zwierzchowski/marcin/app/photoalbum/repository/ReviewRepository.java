package pl.zwierzchowski.marcin.app.photoalbum.repository;

import org.springframework.data.repository.CrudRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Long> {
}
