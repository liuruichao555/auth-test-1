package com.cdd.test.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * UserService
 *
 * @author liuruichao
 * Created on 2018/7/2 21:03
 */
@Service
@Slf4j
public class UserService {
    @Value("${auth.service.baseUrl}")
    private String authServiceBaseUrl;

    private String getHeader(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

    public ResponseEntity<String> login(String username, String password) {
        String url = authServiceBaseUrl + "/oauth/token";
        try {
            HttpResponse response = Request.Post(url)
                    .connectTimeout(1000)
                    .socketTimeout(1000)
                    .bodyForm(buildFormParams(username, password))
                    .execute().returnResponse();
            int statusCode = response.getStatusLine().getStatusCode();
            String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            return ResponseEntity.status(statusCode).body(content);
        } catch (IOException e) {
            log.error("postAccessToken fail!", e);
        }
        return ResponseEntity.status(500).body("Server Error");
    }

    public Iterable<NameValuePair> buildFormParams(String username, String password) {
        List<NameValuePair> list = new ArrayList<>(5);

        list.add(new BasicNameValuePair("grant_type", "password"));
        list.add(new BasicNameValuePair("username", username));
        list.add(new BasicNameValuePair("password", password));
        list.add(new BasicNameValuePair("client_id", "vehicle"));
        list.add(new BasicNameValuePair("client_secret", "vehicle"));

        return list;
    }
}
