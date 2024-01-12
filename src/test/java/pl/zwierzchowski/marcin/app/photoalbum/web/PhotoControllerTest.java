package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
import pl.zwierzchowski.marcin.app.photoalbum.service.PhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;



    @Test
    void givenPhotoModelWithValidId_whenFindPhotoById_thenReturnPhotoWithId() throws Exception {

        //given
        PhotoModel photoModel = new PhotoModel();
        photoModel.setFileName("test Photo");
        Long photoId = 1L;

        //when
        when(photoService.findPhotoById(1L)).thenReturn(photoModel);

        //then
        mockMvc.perform(get("/photos/{userId}", photoId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.fileName", Matchers.is("test Photo")));
    }

    @Test
    void givenValidUserId_whenGetUserPhotos_thenReturnUserPhotos() throws Exception {

        //given
        List<PhotoModel> photoModelList = new ArrayList<>();
        PhotoModel firstPhoto = new PhotoModel();
        firstPhoto.setFileName("first photo");
        PhotoModel secondPhoto = new PhotoModel();
        secondPhoto.setFileName("second photo");
        Long userId = 1L;
        List<PhotoModel> expectedPhotos = Arrays.asList(firstPhoto, secondPhoto);

        //when
        when(photoService.findPhotosByUser(userId)).thenReturn(expectedPhotos);

        //then
        mockMvc.perform(get("/photos/user/{userId}", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fileName").value("first photo"));
    }

    @Test
    void givenUserIdWithNoPhotos_whenGetUserPhotos_thenReturnEmptyList() throws Exception {

        // Given
        Long userId = 2L;

        // When
        when(photoService.findPhotosByUser(userId)).thenReturn(Collections.emptyList());

        // Then
        mockMvc.perform(get("/photos/user/{userId}", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

    }

    @Test
    void givenInvalidUserId_whenGetUserPhotos_thenReturnNotFound() throws Exception {
        // Given
        Long invalidUserId = -1L;

        // When
        when(photoService.findPhotosByUser(invalidUserId)).thenThrow(new ResourceNotFoundException("not found"));

        // Then
        mockMvc.perform(get("/photos/user/{userId}", invalidUserId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));

    }

    @Test
    void givenPendingPhotosExist_whenGetPendingPhotos_thenReturnPendingPhotos() throws Exception {

        // Given
        List<PhotoModel> photoModelList = new ArrayList<>();
        PhotoModel firstPhoto = new PhotoModel();
        firstPhoto.setFileName("first photo");
        PhotoModel secondPhoto = new PhotoModel();
        secondPhoto.setFileName("second photo");
        Long userId = 1L;
        List<PhotoModel> pendingPhotos = Arrays.asList(firstPhoto, secondPhoto);

        // When
        when(photoService.findPendingPhotos()).thenReturn(pendingPhotos);

        // Then
        mockMvc.perform(get("/photos/pending"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fileName").value("first photo"));
    }

    @Test
    void givenNoPendingPhotos_whenGetPendingPhotos_thenReturnEmptyList() throws Exception {

        // Given

        // When
        when(photoService.findPendingPhotos()).thenReturn(Collections.emptyList());

        // Then
        mockMvc.perform(get("/photos/pending"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenValidPhotoId_whenImageDownload_thenReturnImageBytes() throws Exception {
        // Given
        Long validPhotoId = 1L;
        byte[] expectedBytes = "Test image content".getBytes(StandardCharsets.UTF_8);

        //When
        when(photoService.downloadPhoto(validPhotoId)).thenReturn(ResponseEntity.ok(expectedBytes));

        // Then
        mockMvc.perform(get("/photos/download/{id}", validPhotoId))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(content().bytes(expectedBytes));
    }

    @Test
    void givenValidPhotoFile_whenUploadPhoto_thenReturnSavedPhoto() throws Exception {
        // Given
        String description = "Test description";
        String userEmail = "test@example.com";
        byte[] fileContent = "Test file content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", fileContent);
        PhotoModel expectedSavedPhoto = new PhotoModel();
        expectedSavedPhoto.setFileName("test photo");

        // When
        when(photoService.upload(file, description, userEmail)).thenReturn(expectedSavedPhoto);

        // Then
        mockMvc.perform(multipart("/photos/upload")
                        .file(file)
                        .param("description", description)
                        .param("userEmail", userEmail))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fileName", Matchers.is(expectedSavedPhoto.getFileName())));


    }

}