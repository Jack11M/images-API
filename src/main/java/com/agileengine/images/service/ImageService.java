package com.agileengine.images.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import com.agileengine.images.dto.ImageDto;
import com.agileengine.images.dto.ImageInfoDto;
import com.agileengine.images.entity.Image;


/**
 * Abstract interface for image service.
 *
 * @author Yevhenii Petrov
 */
public interface ImageService {

    /**
     * Returns requested quantity of the images as a page result.
     *
     * @param pageable represents count of requested pages (start from 0) and page size (starts from 1).
     * @return list of Images from DB that are represented as {@link ImageDto}
     */
    List<ImageDto> findAll(Pageable pageable);

    /**
     * Retrieves an image by its ID.
     *
     * @param restApiId is Image ID
     * @return Image entity from DB that is represented as {@link ImageInfoDto}
     */
    Optional<ImageInfoDto> findByRestApiId(String restApiId);

    /**
     * Returns all Images if any of their fields contain the given search term.
     *
     * @param searchTerm is a search query.
     * @return list of Images from DB that are represented as {@link ImageInfoDto}
     */
    List<ImageInfoDto> getImagesBySearchParameters(String searchTerm);


    /**
     * Saves all given Images.
     *
     * @param images is list of {@link Image}.
     */
    void saveAll(List<Image> images);
}
