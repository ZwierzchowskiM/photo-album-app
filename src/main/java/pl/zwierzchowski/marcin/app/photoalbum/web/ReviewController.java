package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.ReviewService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("")
    public ResponseEntity<ReviewEntity> createComment (@RequestBody ReviewModel reviewModel) {
        ReviewEntity reviewEntity = reviewService.create(reviewModel);

        return ResponseEntity.ok(reviewEntity);
    }


}
