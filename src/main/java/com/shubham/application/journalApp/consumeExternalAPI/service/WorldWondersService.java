package com.shubham.application.journalApp.consumeExternalAPI.service;

import com.shubham.application.journalApp.consumeExternalAPI.response.WorldWondersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class WorldWondersService {

    private static final String API = "https://www.world-wonders-api.org/v0/wonders/";

    @Autowired
    private RestTemplate restTemplate;

    public List<WorldWondersResponse> getWorldWonders(){
        ResponseEntity<WorldWondersResponse[]> worldWonders = restTemplate.exchange(API, HttpMethod.GET, null, WorldWondersResponse[].class);
        WorldWondersResponse[] body =  worldWonders.getBody();
        return(Arrays.asList(body));
    }
}
