package pl.zwierzchowski.marcin.app.photoalbum.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.types.proto.Album;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.AlbumRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.AlbumMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;

@Service
public class AlbumService {


    AlbumRepository albumRepository;
    AlbumMapper albumMapper;

    public AlbumService(AlbumRepository albumRepository, AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
    }

    private static final List<String> REQUIRED_SCOPES =
            ImmutableList.of("https://www.googleapis.com/auth/photoslibrary.appendonly");




    public AlbumModel createAlbum(String albumName) throws IOException {

        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(
                                FixedCredentialsProvider.create(getUserCredentials()))
                        .build();

        try (
                PhotosLibraryClient photosLibraryClient =
                        PhotosLibraryClient.initialize(settings)) {

            Album createdAlbum = photosLibraryClient.createAlbum(albumName);
            String id = createdAlbum.getId();
            String url = createdAlbum.getProductUrl();
            String title = createdAlbum.getTitle();

//            SharedAlbumOptions options =
//                    // Set the options for the album you want to share
//                    SharedAlbumOptions.newBuilder()
//                            .setIsCollaborative(true)
//                            .setIsCommentable(true)
//                            .build();
//            ShareAlbumResponse response = photosLibraryClient.shareAlbum(id, options);
//
//            // The response contains the shareInfo object, a url, and a token for sharing
//            ShareInfo info = response.getShareInfo();
//            // Link to the shared album
//            String urlShare = info.getShareableUrl();
//            System.out.println(urlShare);

            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setAlbumId(id);
            albumEntity.setUrl(url);
            albumEntity.setAlbumTitle(title);

            albumRepository.save(albumEntity);

            return albumMapper.from(albumEntity);

        } catch (ApiException | IOException e) {
            System.out.println("error");

        }

        return null;
    }


    public AlbumModel getAlbum(String id) throws IOException {

        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(
                                FixedCredentialsProvider.create(getUserCredentials()))
                        .build();

        try (
                PhotosLibraryClient photosLibraryClient =
                        PhotosLibraryClient.initialize(settings)) {

            Album album = photosLibraryClient.getAlbum(id);
            String albumId = album.getId();
            AlbumEntity albumEntity = albumRepository.findByAlbumId(albumId);

            return albumMapper.from(albumEntity);

        } catch (ApiException | IOException e) {
            System.out.println("error");

        }

        return null;
    }

    private static GoogleCredentials getUserCredentials() throws IOException {
        InputStream credentialsStream = new FileInputStream("./client_secret_auth_new.json");
        return GoogleCredentials.fromStream(credentialsStream);
    }

}
