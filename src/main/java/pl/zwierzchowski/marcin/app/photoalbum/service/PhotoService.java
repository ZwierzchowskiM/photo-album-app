package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Status;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
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

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);
    private PhotoRepository photoRepository;
    private S3Service s3Service;
    private PhotoMapper photoMapper;
    private UserRepository userRepository;
    private EmailService emailService;

    public PhotoService(PhotoRepository photoRepository, S3Service s3Service, PhotoMapper photoMapper, UserRepository userRepository, EmailService emailService) {
        this.photoRepository = photoRepository;
        this.s3Service = s3Service;
        this.photoMapper = photoMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public PhotoModel upload(MultipartFile file, String description, String userEmail) {
        logger.info("Uploading photo for user with email {}", userEmail);
        String S3address = "";
        S3address = s3Service.putObject(file);

        PhotoEntity photo = new PhotoEntity();
        photo.setFileName(file.getOriginalFilename());
        photo.setSubmittedDate(LocalDateTime.now());
        photo.setDescription(description);
        photo.setObjectKey(S3address);
        photo.setStatus(Status.PENDING);
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email :" + userEmail + " Not Found"));
        photo.setUser(user);
        photoRepository.save(photo);
        emailService.sendPhotoUploadedToS3(user,photo);

        PhotoModel photoModel = photoMapper.from(photo);
        return photoModel;
    }

    public PhotoModel findPhotoById(Long id) {
        logger.info("Finding photo by ID {}", id);
        PhotoEntity photoEntity = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo with ID :" + id + " Not Found"));
        PhotoModel photoModel = photoMapper.from(photoEntity);

        return photoModel;
    }

    public List<PhotoModel> findPendingPhotos() {
        logger.info("Finding all pending photos");
        List<PhotoEntity> allPendingPhotos = photoRepository.findByStatus(Status.PENDING);
        List<PhotoModel> photoModelList = allPendingPhotos.stream()
                .map(photoMapper::from)
                .toList();

        return photoModelList;
    }

    public List<PhotoModel> findPhotosByUser(Long id) {
        logger.info("Finding photos by user with ID {}", id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID :" + id + " Not Found"));
        List<PhotoEntity> allUserPhotos = photoRepository.findByUser(user);

        List<PhotoModel> photoModelList = allUserPhotos.stream()
                .map(photoMapper::from)
                .toList();

        return photoModelList;
    }

    public ResponseEntity<byte[]> downloadPhoto(Long id) throws UnsupportedEncodingException {
        logger.info("Downloading photo with ID {}", id);
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
        logger.info("Deleting photo with ID {}", id);
        photoRepository.deleteById(id);
    }
}
