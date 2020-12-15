package com.agileengine.images.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@ToString
public class ImageInfoDto {

    @JsonProperty("id")
    private String restApiId;
    private String croppedPicture;
    private String fullPicture;
    private String author;
    private String tags;
}
