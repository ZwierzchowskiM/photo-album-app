package pl.zwierzchowski.marcin.app.photoalbum.repository;

import org.springframework.data.repository.CrudRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
