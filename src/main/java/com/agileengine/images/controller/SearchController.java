package com.agileengine.images.controller;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.agileengine.images.dto.ImageInfoDto;
import com.agileengine.images.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "Search")
@RequestMapping("/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final ImageService imageService;

    @GetMapping("/{searchTerm}")
    @ApiOperation(value = "Find all images by search terms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<ImageInfoDto>> getImagesBySearchTerm(@PathVariable("searchTerm")
                                                                    @NotEmpty String searchTerm) {

        List<ImageInfoDto> images = imageService.getImagesBySearchParameters(searchTerm);
        return images.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(images, HttpStatus.OK);
    }

}
