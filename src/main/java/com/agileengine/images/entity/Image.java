package com.agileengine.images.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Image {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rest_api_id")
    @JsonProperty("id")
    private String restApiId;

    @Column(name = "cropped_picture")
    @JsonProperty("cropped_picture")
    private String croppedPicture;

    @Column(name = "full_picture")
    @JsonProperty("full_picture")
    private String fullPicture;

    @Column(name = "author")
    private String author;

    @Column(name = "tags")
    private String tags;

    @Column(name = "camera")
    private String camera;

    @Column(name = "created", columnDefinition = "DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime created;

    @Column(name = "updated", columnDefinition = "DATE", nullable = false)
    @LastModifiedDate
    private LocalDateTime updated;

    @PrePersist
    private void onPrePersist() {
        setCreated(LocalDateTime.now());
        setUpdated(LocalDateTime.now());
    }

    @PreUpdate
    private void onPreUpdate() {
        setUpdated(LocalDateTime.now());
    }
}
