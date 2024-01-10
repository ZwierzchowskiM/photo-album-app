package pl.zwierzchowski.marcin.app.photoalbum.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
import pl.zwierzchowski.marcin.app.photoalbum.service.GPhotosAlbumService;
import pl.zwierzchowski.marcin.app.photoalbum.service.PhotoService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;


    @Test
    void uploadPhoto() {
    }


    @Test
    void givenPhotoModelWithValidId_whenFindPhotoById_thenReturnPhotoWithId() throws Exception {

        //given
        PhotoModel photoModel = new PhotoModel();
        photoModel.setFileName("test Photo");

        //when
        when(photoService.findPhotoById(1L)).thenReturn(photoModel);

        //then
        mockMvc.perform(get("/photos/1" ))
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
        List<PhotoModel> expectedPhotos = Arrays.asList(firstPhoto,secondPhoto);

        //when
        when(photoService.findPhotosByUser(userId)).thenReturn(expectedPhotos);

        //then
        mockMvc.perform(get("/photos/user/1" ))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fileName").value("first photo"));  // Sprawdzanie właściwości obiektu w liście// Sprawdzanie, czy lista ma rozmiar 1;

    }

    @Test
    void givenUserIdWithNoPhotos_whenGetUserPhotos_thenReturnEmptyList() throws Exception {

        // Given
        Long userId = 2L;

        // When
        when(photoService.findPhotosByUser(userId)).thenReturn(Collections.emptyList());

        // Then
        mockMvc.perform(get("/user/{userId}", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isEmpty());

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
    void getPendingPhotos() {
    }

    @Test
    void imageDownload() {
    }

    @Test
    void deletePhoto() {
    }
}