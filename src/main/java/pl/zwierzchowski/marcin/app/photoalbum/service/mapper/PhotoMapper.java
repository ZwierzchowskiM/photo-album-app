package pl.zwierzchowski.marcin.app.photoalbum.service.mapper;

import org.modelmapper.ModelMapper;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;

public class PhotoMapper {

    public PhotoModel from(PhotoEntity photoEntity) {
        ModelMapper modelMapper = new ModelMapper();
        PhotoModel photoModel = modelMapper.map(photoEntity, PhotoModel.class);
        return photoModel;
    }

    public PhotoEntity from(PhotoModel photoModel) {
        ModelMapper modelMapper = new ModelMapper();
        PhotoEntity photoEntity = modelMapper.map(photoModel, PhotoEntity.class);
        return photoEntity;
    }


}
