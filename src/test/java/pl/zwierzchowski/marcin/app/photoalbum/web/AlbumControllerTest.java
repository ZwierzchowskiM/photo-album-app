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
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

    @SpringBootTest
    @AutoConfigureMockMvc
    class AlbumControllerTest {


        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private GPhotosAlbumService gPhotosAlbumService;


        @Test
        void givenAlbumName_whenPostAlbum_thenReturnAlbum() throws Exception {

            //given
            AlbumModel albumModel = new AlbumModel();
            albumModel.setAlbumTitle("new Album");


            when(gPhotosAlbumService.createAlbum("new Album")).thenReturn(albumModel);

            mockMvc.perform(post("/albums/" )
                            .param("name", "new Album"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.albumTitle", Matchers.is("new Album")));

        }
        @Test
        void givenAlbumModelWithValidId_whenGetAlbum_thenReturnAlbumWithId() throws Exception {

            //given
            AlbumModel albumModel = new AlbumModel();
            albumModel.setAlbumId("0okm1qaz");

            when(gPhotosAlbumService.getAlbum("0okm1qaz")).thenReturn(albumModel);

            mockMvc.perform(get("/albums/0okm1qaz" ))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.albumId", Matchers.is("0okm1qaz")));

        }

        @Test
        void givenAlbumModelWithInvalidId_whenGetAlbum_thenThrowException() throws Exception {

            //given
            AlbumModel albumModel = new AlbumModel();
            albumModel.setAlbumId("123");

            when(gPhotosAlbumService.getAlbum("0okm1qaz")).thenThrow(new ResourceNotFoundException("not found"));

            mockMvc.perform(get("/albums/0okm1qaz" ))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));

        }

        @Test
        void getAlbums() {

        }
    }
