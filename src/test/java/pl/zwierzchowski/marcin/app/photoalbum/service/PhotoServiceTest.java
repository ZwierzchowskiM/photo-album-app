package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.PhotoMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
@AutoConfigureMockMvc
class PhotoServiceTest {

    @MockBean
    private PhotoRepository photoRepository;
    @MockBean
    private S3Service s3Service;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailService emailService;
    @MockBean
    private PhotoMapper photoMapper;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GooglePhotosAlbumService gPhotosAlbumService;

    @Test
    void givenValidInput_whenUploadPhoto_thenSavePhotoAndSendNotification() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        String description = "Test Description";
        String userEmail = "test@example.com";

        UserEntity user = new UserEntity();
        user.setEmail(userEmail);

        // When
        when(s3Service.putObject(file)).thenReturn("S3Address");
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        PhotoModel result = photoService.upload(file, description, userEmail);

        // Then
        assertEquals(file.getOriginalFilename(), result.getFileName());
        assertEquals(description, result.getDescription());
        assertEquals("S3Address", result.getObjectKey());
        assertEquals(userEmail, user.getEmail());

        // Verify interactions
        verify(photoRepository, times(1)).save(any(PhotoEntity.class));
        verify(emailService, times(1)).sendPhotoUploadedToS3(eq(user), any(PhotoEntity.class));
    }

    @Test
    void givenExistingPhotoId_whenFindPhotoById_thenReturnPhotoModel() {
        // Given
        Long photoId = 1L;
        PhotoEntity testPhoto = new PhotoEntity();
        PhotoModel testPhotoModel = new PhotoModel();

        // When
        when(photoRepository.findById(photoId)).thenReturn(Optional.of(testPhoto));
        when(photoMapper.from(testPhoto)).thenReturn(testPhotoModel);

        PhotoModel result = photoService.findPhotoById(photoId);

        // Then
        assertEquals(testPhotoModel, result);

        // Verify interactions
        verify(photoRepository, times(1)).findById(photoId);
        verify(photoMapper, times(1)).from(testPhoto);
    }

    @Test
    void givenNonExistingPhotoId_whenFindPhotoById_thenThrowResourceNotFoundException() {
        // Given
        Long nonExistingPhotoId = 99L;

        // When
        when(photoRepository.findById(nonExistingPhotoId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> {
            photoService.findPhotoById(nonExistingPhotoId);
        });

        // Verify interactions
        verify(photoRepository, times(1)).findById(nonExistingPhotoId);
    }


    @Test
    void givenPendingPhotosExist_whenFindPendingPhotos_thenReturnPendingPhotoModels() {
        // Given
        PhotoEntity pendingPhoto1 = new PhotoEntity();
        pendingPhoto1.setStatus(Status.PENDING);
        PhotoEntity pendingPhoto2 = new PhotoEntity();
        pendingPhoto2.setStatus(Status.PENDING);
        List<PhotoEntity> pendingPhotos = Arrays.asList(pendingPhoto1, pendingPhoto2);

        PhotoModel pendingPhotoModel1 = new PhotoModel();
        pendingPhotoModel1.setStatus(Status.PENDING);
        PhotoModel pendingPhotoModel2 = new PhotoModel();
        pendingPhotoModel2.setStatus(Status.PENDING);
        List<PhotoModel> expectedPhotoModels = Arrays.asList(pendingPhotoModel1, pendingPhotoModel2);

        // When
        when(photoRepository.findByStatus(Status.PENDING)).thenReturn(pendingPhotos);
        when(photoMapper.from(pendingPhoto1)).thenReturn(expectedPhotoModels.get(0));
        when(photoMapper.from(pendingPhoto2)).thenReturn(expectedPhotoModels.get(1));

        // Then
        List<PhotoModel> result = photoService.findPendingPhotos();
        assertEquals(expectedPhotoModels, result);

        // Verify interactions
        verify(photoRepository, times(1)).findByStatus(Status.PENDING);
        verify(photoMapper, times(1)).from(pendingPhoto1);
        verify(photoMapper, times(1)).from(pendingPhoto2);
    }

    @Test
    void findPhotosByUser() {
    }

    @Test
    void downloadPhoto() {
    }

    @Test
    void deletePhoto() {
    }
}