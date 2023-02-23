package com.akashsyncr.Demo.project.service;

import com.akashsyncr.Demo.project.model.User;
import com.akashsyncr.Demo.project.reposistry.UserRepository;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import jdk.jfr.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ImgurApiService {

    @Autowired
    private OAuth20Service oAuth20Service;
    @Autowired
    private UserRepository userRepository;

    public String uploadImage(File file) throws IOException, InterruptedException, ExecutionException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.imgur.com/3/image");
        request.addHeader("Authorization", "Bearer " + oAuth20Service.getAccessToken().getAccessToken());
        request.addHeader("Content-Type", "multipart/form-data");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("image", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
        builder.addTextBody("album", user.getUsername());
        HttpEntity entity = builder.build();
        request.setEntity(entity);
        Response response = oAuth20Service.execute(request);
        JSONObject json = new JSONObject(response.getBody());
        return json.getJSONObject("data").getString("link");
    }

    public List<String> getImagesForUser() throws InterruptedException, ExecutionException, IOException {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.imgur.com/3/account/me/images");
        request.addHeader("Authorization", "Bearer " + oAuth20Service.getAccessToken().getAccessToken());
        Response response = oAuth20Service.execute(request);
        JSONObject json = new JSONObject(response.getBody());
        JSONArray data = json.getJSONArray("data");
        List<String> images = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            images.add(data.getJSONObject(i).getString("link"));
        }
        return images;
    }

    public void deleteImage(String deleteHash) throws InterruptedException, ExecutionException, IOException {
        OAuthRequest request = new OAuthRequest(Verb.DELETE, "https://api.imgur.com/3/image/" + deleteHash);
        request.addHeader("Authorization", "Bearer " + oAuth20Service.getAccessToken().getAccessToken());
        oAuth20Service.execute(request);
    }

}
