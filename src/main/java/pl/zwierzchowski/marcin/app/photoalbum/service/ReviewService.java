package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.ReviewRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.ReviewMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;
import java.time.ZonedDateTime;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PhotoRepository photoRepository;
    private final NotificationService notificationService;
    private final ReviewMapper reviewMapper;
    private final GooglePhotosService googlePhotosService;

    public ReviewService(ReviewRepository reviewRepository, PhotoRepository photoRepository, NotificationService notificationService, ReviewMapper reviewMapper, GooglePhotosService googlePhotosService) {
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;
        this.notificationService = notificationService;
        this.reviewMapper = reviewMapper;
        this.googlePhotosService = googlePhotosService;
    }

    public ReviewModel findReviewById(Long id) {

        ReviewEntity reviewEntity = reviewRepository.findById(id).orElseThrow();
        ReviewModel reviewModel = reviewMapper.from(reviewEntity);

        return reviewModel;
    }

    public ReviewEntity create(ReviewModel reviewModel) {

        ReviewEntity review = reviewMapper.from(reviewModel);
        PhotoEntity photo = photoRepository.findById(reviewModel.getPhotoId()).orElseThrow();

        photo.setReviewResult(review.getResult());
        photo.setComment(review.getComment());
        photo.setStatus(Status.COMPLETED);

        review.setPhotoEntity(photo);
        review.setAlbum(review.getAlbum());
        review.setCreatedDate(ZonedDateTime.now());
        ReviewEntity savedReview = reviewRepository.save(review);

        if(reviewModel.getResult().equals(Result.ACCEPTED)){
            String album = reviewModel.getAlbum();
            googlePhotosService.uploadItemToAlbum(photo,album);
        }

        return savedReview;

    }

    public ReviewModel updateReview(Integer id, ReviewModel reviewModel) {
        return null;
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}



