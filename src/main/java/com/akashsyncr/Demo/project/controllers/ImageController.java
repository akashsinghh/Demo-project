package com.akashsyncr.Demo.project.controllers;

import com.akashsyncr.Demo.project.model.Image;
import com.akashsyncr.Demo.project.model.User;
import com.akashsyncr.Demo.project.reposistry.ImageRepository;
import com.akashsyncr.Demo.project.reposistry.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final WebClient webClient;
    public ImageController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/images")
    public String uploadImage(@RequestBody byte[] imageData, OAuth2AuthenticationToken authentication) {
        String accessToken = authentication.getAccessToken().getTokenValue();

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("image", new ByteArrayResource(imageData));

        String imageUrl = webClient.post()
                .uri("https://api.imgur.com/3/upload")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(data))
                .retrieve()
                .bodyToMono(ImageUploadResponse.class)
                .block()
                .getData()
                .getLink();

        return imageUrl;
    }
    private static class ImageUploadResponse {
        private javax.xml.crypto.Data data;

        public javax.xml.crypto.Data getData() {
            return data;
        }

        public void setData(javax.xml.crypto.Data data) {
            this.data = data;
        }
    }
    private static class Data {
        private String link;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id, Authentication authentication) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent() && image.get().getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.ok(image.get().getData());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id,Authentication authentication) {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent() && image.get().getUser().getUsername().equals(authentication.getName())) {
            imageRepository.delete(image.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/user")
    public ResponseEntity<Li    st<Image>> getImagesForCurrentUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Image> images = imageRepository.findByUserId(user.getId());
        return ResponseEntity.ok(images);
    }
}
