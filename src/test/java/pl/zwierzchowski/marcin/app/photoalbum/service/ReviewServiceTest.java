package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.ReviewRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.PhotoMapper;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.ReviewMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
@AutoConfigureMockMvc
class ReviewServiceTest {

    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ReviewMapper reviewMapper;
    @MockBean
    private PhotoRepository photoRepository;
    @MockBean
    private EmailService emailService;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void givenExistingReviewEntity_whenFindReviewById_thenReturnReviewModel() {

        // Given
        long id = 1L;
        ReviewEntity mockReviewEntity = new ReviewEntity();
        mockReviewEntity.setId(id);
        ReviewModel mockReviewModel = new ReviewModel();
        mockReviewModel.setPhotoId(id);

        // When
        when(reviewRepository.findById(id)).thenReturn(Optional.of(mockReviewEntity));
        when(reviewMapper.from(mockReviewEntity)).thenReturn(mockReviewModel);
        ReviewModel result = reviewService.findReviewById(id);

        // Then
        assertEquals(mockReviewModel, result);
    }

    @Test
    public void givenNonexistentReviewEntity_whenFindReviewById_thenThrowResourceNotFoundException() {
        // Given
        long id = 1L;
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> reviewService.findReviewById(id));
    }


    @Test
    void givenExistingPhoto_whenCreate_thenCreateReviewEntityAndSendEmail() {

        // Given
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setPhotoId(1L);
        reviewModel.setResult("Accepted");
        reviewModel.setComment("Great photo!");
        reviewModel.setAlbum("Vacation Album");
        PhotoEntity mockPhotoEntity = new PhotoEntity();
        mockPhotoEntity.setId(1L);

        ReviewEntity mockReviewEntity = new ReviewEntity();
        mockReviewEntity.setId(1L);
        mockReviewEntity.setComment("Great photo!");

        // When
        when(reviewMapper.from(reviewModel)).thenReturn(mockReviewEntity);
        when(photoRepository.findById(1L)).thenReturn(Optional.of(mockPhotoEntity));
        when(reviewRepository.save(mockReviewEntity)).thenReturn(mockReviewEntity);

        ReviewModel result = reviewService.create(reviewModel);

        // Then
        assertEquals(Status.COMPLETED, mockPhotoEntity.getStatus());
        assertEquals(mockReviewEntity.getPhotoEntity(), mockPhotoEntity);
        assertEquals(reviewModel.getComment(), mockReviewEntity.getComment());
    }

    @Test
    public void givenNonexistentPhoto_whenCreateReviewModel_thenThrowResourceNotFoundException() {
        // Given
        long photoId = -1L;
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setPhotoId(photoId);
        reviewModel.setResult("Accepted");


        // Ustawienie, że nie ma takiego zdjęcia w repozytorium
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> reviewService.create(reviewModel));


    }

}