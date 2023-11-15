package pl.zwierzchowski.marcin.app.photoalbum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;

import java.util.List;

public interface PhotoRepository extends CrudRepository<PhotoEntity, Long> {

    List<PhotoEntity> findByStatus(Status status);

}
