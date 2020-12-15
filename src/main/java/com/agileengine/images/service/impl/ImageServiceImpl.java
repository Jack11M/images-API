package com.agileengine.images.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.agileengine.images.dto.ImageDto;
import com.agileengine.images.dto.ImageInfoDto;
import com.agileengine.images.entity.Image;
import com.agileengine.images.repository.ImageRepository;
import com.agileengine.images.service.ImageService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ImageDto> findAll(Pageable pageable) {
        List<Image> images = imageRepository.findAll(pageable).getContent();
        return mapImagesToImageDtos(images);
    }

    @Override
    public Optional<ImageInfoDto> findByRestApiId(String restApiId) {
        final Optional<Image> optionalImage = imageRepository.findByRestApiId(restApiId);
        return optionalImage.flatMap(image -> Optional.ofNullable(modelMapper.map(image, ImageInfoDto.class)));
    }

    @Override
    public List<ImageInfoDto> getImagesBySearchParameters(String searchTerm) {
        List<Image> images =
                imageRepository.findByAllFields(searchTerm);
        return mapImagesToImageInfoDtos(images);
    }

    @Override
    public void saveAll(List<Image> images) {
        imageRepository.saveAll(images);
    }

    private List<ImageDto> mapImagesToImageDtos(Iterable<Image> images) {
        return modelMapper.map(images, new TypeToken<List<ImageDto>>() {
        }.getType());
    }

    private List<ImageInfoDto> mapImagesToImageInfoDtos(Iterable<Image> images) {
        return modelMapper.map(images, new TypeToken<List<ImageInfoDto>>() {
        }.getType());
    }
}
