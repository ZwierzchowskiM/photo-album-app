package pl.zwierzchowski.marcin.app.photoalbum.service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.entity.AlbumEntity;
import pl.zwierzchowski.marcin.app.photoalbum.web.model.AlbumModel;


@Service
public class AlbumMapper {

    public AlbumModel from(AlbumEntity albumEntity) {
        ModelMapper modelMapper = new ModelMapper();
        AlbumModel albumModel = modelMapper.map(albumEntity, AlbumModel.class);
        return albumModel;
    }

    public AlbumEntity from(AlbumModel albumModel) {
        ModelMapper modelMapper = new ModelMapper();
        AlbumEntity albumEntity = modelMapper.map(albumModel, AlbumEntity.class);
        return albumEntity;
    }


}
