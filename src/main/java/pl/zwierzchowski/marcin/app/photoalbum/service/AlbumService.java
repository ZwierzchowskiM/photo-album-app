package pl.zwierzchowski.marcin.app.photoalbum.service;

import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import com.google.photos.types.proto.Album;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.AlbumRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.AlbumMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;

import com.google.api.client.auth.oauth2.Credential;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;



//-----------------------------
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;


@Service
public class AlbumService {

    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(AlbumService.class.getResource("/").getPath(), "credentials");

//    private static final java.io.File DATA_STORE_DIR =
//            new java.io.File(AlbumService.class.getResource("/").getPath(), "credentials");
//

    private String path = new String("./client_secret_auth_new.json");
    private String path2 = new String("./client_secret.json");
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final int LOCAL_RECEIVER_PORT = 61984;

    AlbumRepository albumRepository;
    AlbumMapper albumMapper;

    public AlbumService(AlbumRepository albumRepository, AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
    }

    private static final List<String> REQUIRED_SCOPES =
            ImmutableList.of("https://www.googleapis.com/auth/photoslibrary");
    private String scope = "https://www.googleapis.com/auth/photoslibrary.appendonly";

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


    public AlbumModel getAlbum(String id) throws IOException, GeneralSecurityException {

        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(
//                                FixedCredentialsProvider.create(getUserCredentialsNew(path2,REQUIRED_SCOPES)))
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

    public void uploadPhoto() throws IOException {

        String albumId = "AD3RILPn5iZQzx9MedyntRmIC_SlrOMQFbNF6zEQKlTavRRRj7APa0WS1a33thyWS9-ep084-2fu";
        String mimeType = "image/jpg";


        PhotosLibrarySettings settings =
                PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(
                                FixedCredentialsProvider.create(getUserCredentials()))
                        .build();


        try (PhotosLibraryClient photosLibraryClient =
                     PhotosLibraryClient.initialize(settings)) {


            RandomAccessFile randomAccessFile = new RandomAccessFile("c:\\data\\test.jpg", "r");
            File file = new File("./test.jpg");
//            RandomAccessFile randomAccessFile2 = new RandomAccessFile("./test.jpeg", "r");

            UploadMediaItemRequest uploadRequest = UploadMediaItemRequest.newBuilder()
                    .setMimeType(mimeType)
                    .setDataFile(randomAccessFile)
                    .build();

            UploadMediaItemResponse uploadResponse = photosLibraryClient.uploadMediaItem(uploadRequest);
            if (uploadResponse.getError().isPresent()) {
                // If the upload results in an error, handle it
                UploadMediaItemResponse.Error error = uploadResponse.getError().get();
            } else {
                // If the upload is successful, get the uploadToken
                String uploadToken = uploadResponse.getUploadToken().get();
                // Use this upload token to create a media item
            }

            String uploadToken = String.valueOf(uploadResponse.getUploadToken());

            NewMediaItem newMediaItem = NewMediaItemFactory
                    .createNewMediaItem(uploadToken, "test.jpg", "test");
            List<NewMediaItem> newItems = Arrays.asList(newMediaItem);

            photosLibraryClient.batchCreateMediaItems(albumId, newItems);

            System.out.println("Plik został przesłany i dodany do albumu.");;

        } catch (ApiException | IOException e) {
            System.out.println("error");
        }

//        return null;
    }


    private static GoogleCredentials getUserCredentials() throws IOException {
        InputStream credentialsStream = new FileInputStream("./client_secret_auth_new.json");
        return GoogleCredentials.fromStream(credentialsStream);
    }

    private static Credentials getUserCredentialsNew(String credentialsPath, List<String> selectedScopes)
            throws IOException, GeneralSecurityException {
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsPath)));
        String clientId = clientSecrets.getDetails().getClientId();
        String clientSecret = clientSecrets.getDetails().getClientSecret();

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        clientSecrets,
                        selectedScopes)
                        .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder().setPort(LOCAL_RECEIVER_PORT).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(credential.getRefreshToken())
                .build();
    }

}
