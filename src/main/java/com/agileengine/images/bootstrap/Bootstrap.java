package com.agileengine.images.bootstrap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.agileengine.images.entity.Image;
import com.agileengine.images.util.AppConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import com.agileengine.images.service.ImageService;

@Component
@RequiredArgsConstructor
@Slf4j
public class Bootstrap {

    private final ImageService imageService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Set<Image> savedImages = new HashSet<>();

    @Value("${agile.engine.rest.uri}")
    private String agileEngineRestUri;

    @Value("${api.key.value}")
    private String apiKeyValue;

    private JSONArray imagesJsonArray;

    @PostConstruct
    @Scheduled(cron = AppConstants.CRON_TIME)
    public void initImageLocalCache() throws JsonProcessingException {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(AppConstants.API_KEY, apiKeyValue);

        final HttpEntity<String> authHttpEntity = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        final String authResultAsJsonString = restTemplate.exchange(agileEngineRestUri + AppConstants.AUTH_PATH,
                HttpMethod.POST, authHttpEntity, String.class).getBody();

        final JsonNode jsonNode = objectMapper.readTree(authResultAsJsonString);
        final String authToken = jsonNode.path(AppConstants.TOKEN).asText();
        httpHeaders.add(AppConstants.AUTHORIZATION, AppConstants.BEARER + authToken);

        final HttpEntity<String> imagesHttpEntity = new HttpEntity<>(httpHeaders);
        int pageNumber = 0;
        while (isApiHasMoreImages(pageNumber, imagesHttpEntity)) {
            try {
                pageNumber++;
                saveImagesFromJson(httpHeaders, imagesJsonArray);
            } catch (JsonProcessingException e) {
                log.error("An error occurred while saving images from Rest Api Json", e);
                e.printStackTrace();
            }
        }

        imageService.saveAll(new ArrayList<>(savedImages));
    }

    private boolean isApiHasMoreImages(int pageNumber, HttpEntity<String> imagesHttpEntity) {
        final String url = String.format("%s/%s?%s=%s", agileEngineRestUri, AppConstants.IMAGES, AppConstants.PAGE,
                pageNumber);
        final String imagesResultAsJsonString = restTemplate.exchange(url, HttpMethod.GET, imagesHttpEntity,
                String.class).getBody();
        final JSONObject imagesJsonObject = new JSONObject(imagesResultAsJsonString);
        imagesJsonArray = imagesJsonObject.getJSONArray(AppConstants.PICTURES);
        return imagesJsonObject.getBoolean(AppConstants.HAS_MORE);
    }

    private void saveImagesFromJson(HttpHeaders httpHeaders, JSONArray imagesJsonArray) throws JsonProcessingException {
        List<Image> images = objectMapper.readValue(imagesJsonArray.toString(), new TypeReference<List<Image>>() {});
        images.forEach(image -> {
            final HttpEntity<String> imageHttpEntity = new HttpEntity<>(httpHeaders);
            final String url = String.format("%s/%s/%s", agileEngineRestUri, AppConstants.IMAGES,
                    image.getRestApiId());
            final String imageResultAsJsonString = restTemplate.exchange(url, HttpMethod.GET, imageHttpEntity,
                    String.class).getBody();
            try {
                final Image fullImage = objectMapper.readValue(imageResultAsJsonString, Image.class);
                savedImages.add(fullImage);
            } catch (JsonProcessingException e) {
                log.error("An error occurred while saving image with rest api id {}", image.getRestApiId(), e);
                e.printStackTrace();
            }
        });
    }
}
