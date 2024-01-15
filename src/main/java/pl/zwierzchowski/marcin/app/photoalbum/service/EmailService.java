package pl.zwierzchowski.marcin.app.photoalbum.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.PhotoEntity;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.Email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${spring.mail.username}") private String sender;

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
    }



    private Email createEmailPhotoUploaded (UserEntity user, PhotoEntity photo) {

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

    public void sendPhotoUploadedToS3(UserEntity user, PhotoEntity photo)
    {
        Email email = createEmailPhotoUploaded(user, photo);
        try {
            sendMail(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
