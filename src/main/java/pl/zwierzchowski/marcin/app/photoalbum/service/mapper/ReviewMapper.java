package pl.zwierzchowski.marcin.app.photoalbum.service.mapper;

import org.springframework.stereotype.Component;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;
import org.modelmapper.ModelMapper;

@Component
public class ReviewMapper {

    public ReviewModel from(ReviewEntity reviewEntity) {
        ModelMapper modelMapper = new ModelMapper();
        ReviewModel reviewModel = modelMapper.map(reviewEntity, ReviewModel.class);
        return reviewModel;
    }

    public ReviewEntity from(ReviewModel reviewModel) {
        ModelMapper modelMapper = new ModelMapper();
        ReviewEntity reviewEntity = modelMapper.map(reviewModel, ReviewEntity.class);
        return reviewEntity;
    }




}
