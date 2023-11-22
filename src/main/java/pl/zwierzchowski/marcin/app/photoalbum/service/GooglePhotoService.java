package pl.zwierzchowski.marcin.app.photoalbum.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.types.proto.Album;
import com.google.api.gax.core.FixedCredentialsProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.google.auth.oauth2.UserCredentials;
import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.AlbumRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;

@Service
public class GooglePhotoService {


    AlbumRepository albumRepository;

    public GooglePhotoService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    private static final List<String> REQUIRED_SCOPES =
            ImmutableList.of("https://www.googleapis.com/auth/photoslibrary.appendonly");


    public AlbumEntity createAlbum(String albumName) throws IOException {

        // Set up the Photos Library Client that interacts with the API
        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(
                                FixedCredentialsProvider.create(getUserCredentials()))
                        .build();

        try (
                PhotosLibraryClient photosLibraryClient =
                        PhotosLibraryClient.initialize(settings)) {

            // Create a new Album  with at title
            Album createdAlbum = photosLibraryClient.createAlbum(albumName);
            // Get some properties from the album, such as its ID and product URL
            String id = createdAlbum.getId();
            String url = createdAlbum.getProductUrl();

            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setAlbumId(id);
            albumEntity.setUrl(url);

            return albumEntity;

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
