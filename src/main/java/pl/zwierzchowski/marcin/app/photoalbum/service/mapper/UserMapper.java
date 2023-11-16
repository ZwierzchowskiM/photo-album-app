package pl.zwierzchowski.marcin.app.photoalbum.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.UserEntity;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.UserModel;
@Service
public class UserMapper {

    public UserModel from(UserEntity userEntity) {
        ModelMapper modelMapper = new ModelMapper();
        UserModel userModel = modelMapper.map(userEntity, UserModel.class);
        return userModel;
    }

    public UserEntity from(UserModel userModel) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userModel, UserEntity.class);
        return userEntity;
    }


}
