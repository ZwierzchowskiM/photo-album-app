package pl.zwierzchowski.marcin.app.photoalbum.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.Credentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.types.proto.Album;
import com.google.api.gax.core.FixedCredentialsProvider;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import com.google.auth.oauth2.UserCredentials;


public class GooglePhotosService {

    /** OAuth scopes to request. This demo requires `photoslibrary.appendonly` at a minimum. */
    private static final List<String> REQUIRED_SCOPES =
            ImmutableList.of("https://www.googleapis.com/auth/photoslibrary.appendonly");

    public void test() throws IOException {


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
            Album createdAlbum = photosLibraryClient.createAlbum("My Album");

            // Get some properties from the album, such as its ID and product URL
            String id = createdAlbum.getId();
            String url = createdAlbum.getProductUrl();

        } catch (ApiException | IOException e) {
            // Error during album creation
        }


    }

    public GooglePhotosService() throws IOException {
    }

    private Credentials getUserCredentials()  {

        return UserCredentials.newBuilder()
                .setClientId("your client id")
                .setClientSecret("your client secret")
                .build();
    }
}
