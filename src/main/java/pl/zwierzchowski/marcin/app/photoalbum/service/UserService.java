package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.zwierzchowski.marcin.app.photoalbum.exceptions.ResourceNotFoundException;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.UserMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserModel;

import java.util.Optional;

@Component
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserModel register(UserModel registration) {
        logger.info("Registering new user");
        UserEntity savedUser = new UserEntity();
        savedUser.setFirstName(registration.getFirstName());
        savedUser.setLastName(registration.getLastName());
        savedUser.setEmail(registration.getEmail());
        userRepository.save(savedUser);

        UserModel savedUserModel = userMapper.from(savedUser);

        return savedUserModel;
    }

    public UserModel findUserById(Long id) {
        logger.info("Finding user by ID {}", id);
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID :" + id + " Not Found"));;
        UserModel userModel = userMapper.from(user);

        return userModel;

    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID {}", id);
        userRepository.deleteById(id);
    }

}
