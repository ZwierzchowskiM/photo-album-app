package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.ReviewRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.ReviewMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PhotoRepository photoRepository;
    private final NotificationService notificationService;
    private final ReviewMapper reviewMapper;


    public ReviewService(ReviewRepository reviewRepository, PhotoRepository photoRepository, NotificationService notificationService, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;
        this.notificationService = notificationService;
        this.reviewMapper = reviewMapper;
    }



    public ReviewEntity create(ReviewModel reviewModel) {

        ReviewEntity reviewEntity = reviewMapper.from(reviewModel);
        PhotoEntity photoEntity = photoRepository.findById(reviewModel.getPhotoId()).orElseThrow();
        photoEntity.setStatus(Status.COMPLETED);
        photoEntity.setReviewResult(reviewEntity);
        reviewEntity.setPhotoEntity(photoEntity);
        reviewEntity.setCreatedDate(ZonedDateTime.now());

        ReviewEntity savedReview = reviewRepository.save(reviewEntity);

        return savedReview;

    }

    public ReviewModel read(Integer id) {
        return null;
    }

    public ReviewModel update(Integer id, ReviewModel reviewModel) {
        return null;
    }

    public void delete(Integer id) {
    }

    public List<ReviewModel> list() {
        return null;
    }


}



