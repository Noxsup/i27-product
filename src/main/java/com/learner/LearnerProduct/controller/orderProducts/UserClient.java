package com.learner.LearnerProduct.controller.orderProducts;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserClient {
    public UserById getUserDetails(RestTemplate restTemplate, long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String USER_SERVICE_URL = "http://learner-user-api/api/users/getUserById";
        ResponseEntity<UserById> response = restTemplate.exchange(USER_SERVICE_URL + "?userId={userId}", HttpMethod.GET, entity, UserById.class, userId);

        return response.getBody();
    }
}
