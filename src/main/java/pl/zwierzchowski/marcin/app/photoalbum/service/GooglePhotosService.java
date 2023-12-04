package pl.zwierzchowski.marcin.app.photoalbum.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.BatchCreateMediaItemsResponse;
import com.google.photos.library.v1.proto.NewMediaItem;
import com.google.photos.library.v1.proto.NewMediaItemResult;
import com.google.photos.library.v1.proto.ShareAlbumResponse;
import com.google.photos.library.v1.upload.UploadMediaItemRequest;
import com.google.photos.library.v1.upload.UploadMediaItemResponse;
import com.google.photos.library.v1.util.NewMediaItemFactory;
import com.google.photos.types.proto.Album;
import com.google.photos.types.proto.ShareInfo;
import com.google.photos.types.proto.SharedAlbumOptions;
import com.google.rpc.Code;
import com.google.rpc.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.AlbumRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.AlbumMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GooglePhotosService {

    private static final Logger logger = LoggerFactory.getLogger(GooglePhotosService.class);

    private final GoogleAPICredentialProvider credentialProviderComponent;

    AlbumRepository albumRepository;
    AlbumMapper albumMapper;
    private String albumId;

    public GooglePhotosService(GoogleAPICredentialProvider credentialProviderComponent, AlbumRepository albumRepository, AlbumMapper albumMapper) {
        this.credentialProviderComponent = credentialProviderComponent;
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
    }

    public NewMediaItemResult uploadItemToAlbum() {

        String albumId = "AD3RILPn5iZQzx9MedyntRmIC_SlrOMQFbNF6zEQKlTavRRRj7APa0WS1a33thyWS9-ep084-2fu";

        logger.info("uploading item to google photos album: \"" + "test item" + "\"");
        try {
            PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
                    .setCredentialsProvider(credentialProviderComponent.getCredentialProvider())
                    .build();
            try (PhotosLibraryClient photosLibraryClient = PhotosLibraryClient.initialize(settings);
                 RandomAccessFile file = new RandomAccessFile("c:\\data\\test.jpg", "r")) {
                UploadMediaItemRequest request = UploadMediaItemRequest.newBuilder()
                        .setMimeType("image/png")
                        .setDataFile(file)
                        .build();
                UploadMediaItemResponse response = photosLibraryClient.uploadMediaItem(request);
                if (response.getError().isPresent()) {
                    UploadMediaItemResponse.Error error = response.getError().get();
                    logger.warn("uploadUrlToAlbum error", error.getCause());
                    return null;
                }
                if (response.getUploadToken().isEmpty()) {
                    logger.warn("uploadUrlToAlbum no upload token exists");
                    return null;
                }
                String uploadToken = response.getUploadToken().get();
                NewMediaItem newMediaItem = NewMediaItemFactory.createNewMediaItem(
                        uploadToken, StringUtils.truncate("test", 254), "test");
                BatchCreateMediaItemsResponse createItemsResponse = photosLibraryClient.batchCreateMediaItems(
                        albumId, Collections.singletonList(newMediaItem));
                for (NewMediaItemResult itemsResponse : createItemsResponse.getNewMediaItemResultsList()) {
                    Status status = itemsResponse.getStatus();
                    if (status.getCode() != Code.OK_VALUE) {
                        logger.warn("error creating media item: " + status.getCode() + " " + status.getMessage());
                    }
                    return itemsResponse;
                }
            }
        } catch (IOException | GeneralSecurityException | ApiException theE) {
            logger.warn("uploadUrlToAlbum:", theE);
        }
        return null;
    }


    public AlbumModel getAlbum(String id) throws IOException, GeneralSecurityException {

        logger.info("uploading item to google photos album: \"" + "test item" + "\"");
        try {
            PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
                    .setCredentialsProvider(credentialProviderComponent.getCredentialProvider())
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
        } catch (IOException | GeneralSecurityException | ApiException theE) {
            logger.warn("uploadUrlToAlbum:", theE);
        }
        return null;

    }

    public AlbumModel createAlbum(String albumName) throws IOException {

        logger.info("creating album: \"" + albumName + "\"");
        try {
            PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
                    .setCredentialsProvider(credentialProviderComponent.getCredentialProvider())
                    .build();

            try (
                    PhotosLibraryClient photosLibraryClient =
                            PhotosLibraryClient.initialize(settings)) {

                Album createdAlbum = photosLibraryClient.createAlbum(albumName);
                String id = createdAlbum.getId();
                String url = createdAlbum.getProductUrl();
                String title = createdAlbum.getTitle();

//                SharedAlbumOptions options =
//                        // Set the options for the album you want to share
//                        SharedAlbumOptions.newBuilder()
//                                .setIsCollaborative(true)
//                                .setIsCommentable(true)
//                                .build();
//                ShareAlbumResponse response = photosLibraryClient.shareAlbum(id, options);
//
//                // The response contains the shareInfo object, a url, and a token for sharing
//                ShareInfo info = response.getShareInfo();
//                // Link to the shared album
//                String urlShare = ((ShareInfo) info).getShareableUrl();
//                System.out.println(urlShare);

                AlbumEntity albumEntity = new AlbumEntity();
                albumEntity.setAlbumId(id);
                albumEntity.setUrl(url);
                albumEntity.setAlbumTitle(title);

                albumRepository.save(albumEntity);

                return albumMapper.from(albumEntity);

            } catch (ApiException | IOException e) {
                logger.warn("uploadUrlToAlbum:", e);
            }
        } catch (IOException | GeneralSecurityException | ApiException theE) {
            logger.warn("uploadUrlToAlbum:", theE);

        }
        return null;
    }

    public List<AlbumModel> getAlbums() {

        List<AlbumModel> albums = new ArrayList<>();
        logger.info("get album list");
        try {
            PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
                    .setCredentialsProvider(credentialProviderComponent.getCredentialProvider())
                    .build();
            try (
                    PhotosLibraryClient photosLibraryClient =
                            PhotosLibraryClient.initialize(settings)) {

                for (Album album : photosLibraryClient.listAlbums().iterateAll()) {

                    AlbumEntity albumEntity = albumRepository.findByAlbumId(album.getId());
                    if (albumEntity!=null) {
                        AlbumModel albumModel = albumMapper.from(albumEntity);
                        albums.add(albumModel);
                    }
                }

                return albums;

            } catch (ApiException | IOException e) {
                System.out.println("error");
            }
        } catch (IOException | GeneralSecurityException | ApiException theE) {
            logger.warn("uploadUrlToAlbum:", theE);
        }
        return null;

    }
}