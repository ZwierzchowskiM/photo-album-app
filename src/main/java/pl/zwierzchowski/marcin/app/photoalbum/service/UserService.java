package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Component;
import pl.zwierzchowski.marcin.app.photoalbum.repository.UserRepository;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.UserMapper;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.ReviewModel;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserModel;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserRegistrationModel;

import java.util.Optional;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserEntity register(UserRegistrationModel registration) {
        UserEntity savedUser = new UserEntity();
        savedUser.setFirstName(registration.getFirstName());
        savedUser.setLastName(registration.getLastName());
        savedUser.setEmail(registration.getEmail());
        savedUser.setPassword(registration.getPassword());
        userRepository.save(savedUser);

        return savedUser;
    }

    public UserModel findUserById(Long id) {

        Optional<UserEntity> user = userRepository.findById(id);
        UserModel userModel = userMapper.from(user.get());

        return userModel;

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
