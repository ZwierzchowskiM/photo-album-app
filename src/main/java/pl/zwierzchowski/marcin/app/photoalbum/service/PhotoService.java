package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.repository.PhotoRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.PhotoMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.PhotoModel;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;
    private S3Service s3Service;
    private PhotoMapper photoMapper;
    private UserRepository userRepository;

    public PhotoService(PhotoRepository photoRepository, S3Service s3Service, PhotoMapper photoMapper, UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.s3Service = s3Service;
        this.photoMapper = photoMapper;
        this.userRepository = userRepository;
    }

    public PhotoModel upload(MultipartFile file, String description, String userEmail) {

        String S3address = "";
        S3address = s3Service.putObject(file);

        PhotoEntity photo = new PhotoEntity();
        photo.setFileName(file.getName());
        photo.setSubmittedDate(LocalDateTime.now());
        photo.setDescription(description);
        photo.setObjectKey(S3address);
        photo.setStatus(Status.PENDING);
        UserEntity user = userRepository.findByEmail(userEmail).get(0);
        photo.setUser(user);
        photoRepository.save(photo);

        PhotoModel photoModel = photoMapper.from(photo);
        return photoModel;
    }

    public PhotoModel findPhotoById(Long id) {

        PhotoEntity photoEntity = photoRepository.findById(id).orElseThrow();
        PhotoModel photoModel = photoMapper.from(photoEntity);

        return photoModel;
    }

    public List<PhotoModel> findPendingPhotos() {

        List<PhotoEntity> allPendingPhotos = photoRepository.findByStatus(Status.PENDING);
        List<PhotoModel> photoModelList = allPendingPhotos.stream()
                .map(photoMapper::from)
                .toList();

        return photoModelList;
    }

    public ResponseEntity<byte[]> downloadPhoto(Long id) throws UnsupportedEncodingException {

        PhotoEntity photoEntity = photoRepository.findById(id).orElseThrow();
        String key = photoEntity.getObjectKey();
        byte[] data = s3Service.getObjectBytes(key);

        String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(data.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(data, httpHeaders, HttpStatus.OK);
    }

    public void deletePhoto(Long id) {

        photoRepository.deleteById(id);
    }
}
