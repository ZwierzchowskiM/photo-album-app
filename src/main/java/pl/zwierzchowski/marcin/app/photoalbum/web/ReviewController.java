package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.ReviewService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("")
    public ResponseEntity<ReviewModel> createReview (@RequestBody ReviewModel reviewModel) {

        ReviewModel savedReview = reviewService.create(reviewModel);

        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewModel> getReview(@PathVariable Long id) {

        ReviewModel review = reviewService.findReviewById(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteReview(@PathVariable Long id) {

        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted");
    }

}
