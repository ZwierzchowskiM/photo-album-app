package pl.zwierzchowski.marcin.app.photoalbum.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.zwierzchowski.marcin.app.photoalbum.enums.Result;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.ReviewEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.Email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String sender;

    private JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMail(Email email) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());

        helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process(email.getTemplate(), context);
        helper.setText(html, true);

        emailSender.send(message);

        logger.info("Email sent successfully to {}", email.getTo());
    }

    private Email createEmailPhotoUploaded(UserEntity user, PhotoEntity photo) {

        Email email = new Email();
        email.setTo(user.getEmail());
        email.setFrom(sender);
        email.setSubject("Photo uploaded to S3 service. Awaiting for review");
        email.setTemplate("uploaded-to-s3-template.html");

        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.getFirstName());
        properties.put("fileName", photo.getFileName());
        email.setProperties(properties);

        return email;
    }

    public void sendPhotoUploadedToS3(UserEntity user, PhotoEntity photo) {
        Email email = createEmailPhotoUploaded(user, photo);
        try {
            logger.info("Sending email to {}", user.getEmail());
            sendMail(email);
        } catch (MessagingException e) {
            logger.error("Error while sending email to {}", user.getEmail(), e);
        }

    }

    private Email createEmailPhotoAccepted(ReviewEntity review) {

        Email email = new Email();
        PhotoEntity photo = review.getPhotoEntity();
        UserEntity user = photo.getUser();

        email.setTo(user.getEmail());
        email.setFrom(sender);
        email.setSubject("Photo reviewed");
        email.setTemplate("photo-reviewed-accepted.html");

        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.getFirstName());
        properties.put("fileName", photo.getFileName());
        properties.put("reviewResult", review.getResult());
        properties.put("album", review.getAlbum());
        email.setProperties(properties);

        return email;
    }

    private Email createEmailPhotoRejected(ReviewEntity review) {

        Email email = new Email();
        PhotoEntity photo = review.getPhotoEntity();
        UserEntity user = photo.getUser();

        email.setTo(user.getEmail());
        email.setFrom(sender);
        email.setSubject("Photo reviewed");
        email.setTemplate("photo-reviewed-rejected.html");

        Map<String, Object> properties = new HashMap<>();
        properties.put("name", user.getFirstName());
        properties.put("fileName", photo.getFileName());
        properties.put("reviewResult", review.getResult());
        properties.put("album", review.getAlbum());
        email.setProperties(properties);

        return email;
    }

    public void sendPhotoReviewResult(ReviewEntity review) {

        Email email = new Email();
        if (review.getResult().equals(Result.ACCEPTED)) {
            email = createEmailPhotoAccepted(review);
        } else {
            email = createEmailPhotoRejected(review);
        }

        try {
            logger.info("Sending photo review result email to {}", email.getTo());
            sendMail(email);
        } catch (MessagingException e) {
            logger.error("Error while sending photo review result email to {}", email.getTo(), e);
        }

    }

}
