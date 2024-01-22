package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
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
    private final ReviewMapper reviewMapper;
    private final GooglePhotosAlbumService googlePhotosService;
    private final EmailService emailService;

    public ReviewService(ReviewRepository reviewRepository, PhotoRepository photoRepository,ReviewMapper reviewMapper, GooglePhotosAlbumService googlePhotosService, EmailService emailService) {
        this.reviewRepository = reviewRepository;
        this.photoRepository = photoRepository;
        this.reviewMapper = reviewMapper;
        this.googlePhotosService = googlePhotosService;
        this.emailService = emailService;
    }

    public ReviewModel findReviewById(Long id) {

        ReviewEntity reviewEntity = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with ID :" + id + " Not Found"));;
        ReviewModel reviewModel = reviewMapper.from(reviewEntity);

        return reviewModel;
    }

    public ReviewModel create(ReviewModel reviewModel) {

        ReviewEntity review = reviewMapper.from(reviewModel);
        PhotoEntity photo = photoRepository.findById(reviewModel.getPhotoId())
                .orElseThrow(() -> new ResourceNotFoundException("Photo with ID :" + reviewModel.getPhotoId() + " Not Found"));

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
        emailService.sendPhotoReviewResult(savedReview);

        return reviewMapper.from(savedReview);

    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}



