package com.agileengine.images.controller;

import java.util.List;
import javax.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.agileengine.images.dto.ImageDto;
import com.agileengine.images.dto.ImageInfoDto;
import com.agileengine.images.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "Image")
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @GetMapping()
    @ApiOperation(value = "Requested quantity of the images and return them as a page result")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<ImageDto>> findPaginated(
            @ApiParam(value = "Results page you want to retrieve (0..N). Default value: 0")
            @RequestParam(value = "page", required = false, defaultValue = "0") @PositiveOrZero int page,
            @ApiParam(value = "Number of records per page. Default value: 10")
            @RequestParam(value = "size", required = false, defaultValue = "10") @PositiveOrZero int size) {
        List<ImageDto> images = imageService.findAll(PageRequest.of(page, size));
        return images.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find a full image info by its Rest Api ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<ImageInfoDto> getImageById(
            @ApiParam(value = "Rest Api ID of existed image")
            @PathVariable("id") @PositiveOrZero String restApiId) {
        return ResponseEntity.of(imageService.findByRestApiId(restApiId));
    }
}
