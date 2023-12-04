package pl.zwierzchowski.marcin.app.photoalbum.service;

import org.springframework.stereotype.Service;
import pl.zwierzchowski.marcin.app.photoalbum.repository.AlbumRepository;
import pl.zwierzchowski.marcin.app.photoalbum.service.mapper.AlbumMapper;

@Service
public class AlbumService {

    AlbumRepository albumRepository;
    AlbumMapper albumMapper;

    public AlbumService(AlbumRepository albumRepository, AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
    }
}
