package com.agileengine.images.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.agileengine.images.entity.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

    Optional<Image> findByRestApiId(String restApiId);

    @Query(value = "SELECT i FROM Image i WHERE i.author LIKE %:searchTerm% OR i.tags LIKE %:searchTerm% " +
            "OR i.camera LIKE %:searchTerm% OR i.croppedPicture LIKE %:searchTerm% OR i.fullPicture LIKE %:searchTerm%")
    List<Image> findByAllFields(@Param("searchTerm") String searchTerm);
}
