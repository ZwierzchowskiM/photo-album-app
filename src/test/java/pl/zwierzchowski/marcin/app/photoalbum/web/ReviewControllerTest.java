package pl.zwierzchowski.marcin.app.photoalbum.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;
import pl.zwierzchowski.marcin.app.photoalbum.service.ReviewService;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;


    @Test
    void givenValidReviewModel_whenCreateReview_thenReturnSavedReview() throws Exception {
        // Given
        String comment = "test comment";
        ReviewModel validReviewModel = new ReviewModel();
        validReviewModel.setComment(comment);
        ReviewModel expectedSavedReview = new ReviewModel();
        expectedSavedReview.setComment(comment);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReviewModel = objectMapper.writeValueAsString(expectedSavedReview);

        // When
        when(reviewService.create(validReviewModel)).thenReturn(expectedSavedReview);

        // Then
        mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReviewModel))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", is(1)));
    }

    @Test
    void getReview() {
    }

    @Test
    void deleteReview() {
    }
}