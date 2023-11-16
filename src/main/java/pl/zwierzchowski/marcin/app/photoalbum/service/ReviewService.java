package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.ReviewRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.ReviewMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;
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


    public ReviewModel findReviewById(Long id) {

        ReviewEntity reviewEntity = reviewRepository.findById(id).orElseThrow();
        ReviewModel reviewModel = reviewMapper.from(reviewEntity);

        return reviewModel;
    }

    public ReviewEntity create(ReviewModel reviewModel) {

        ReviewEntity reviewEntity = reviewMapper.from(reviewModel);
        PhotoEntity photoEntity = photoRepository.findById(reviewModel.getPhotoId()).orElseThrow();
        reviewEntity.setPhotoEntity(photoEntity);
        photoEntity.setReviewResult(reviewEntity.getResult());
        photoEntity.setComment(reviewEntity.getComment());
        photoEntity.setStatus(Status.COMPLETED);
        reviewEntity.setCreatedDate(ZonedDateTime.now());

        ReviewEntity savedReview = reviewRepository.save(reviewEntity);

        return savedReview;

    }

    public ReviewModel updateReview(Integer id, ReviewModel reviewModel) {
        return null;
    }



    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}



