package com.shubham.application.journalApp.consumeExternalAPI.controller;

import com.shubham.application.journalApp.consumeExternalAPI.response.WorldWondersResponse;
import com.shubham.application.journalApp.consumeExternalAPI.service.WorldWondersService;
import com.shubham.application.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/externalAPI/worldWonders")
public class WorldWondersController {
    @Autowired
    private WorldWondersService worldWondersService;
    @GetMapping("/getWorldWonders")
    public ResponseEntity<?> getWorldWonders(){
        try{
            List<WorldWondersResponse> allWorldWonders = worldWondersService.getWorldWonders();
            if(allWorldWonders!=null){
                return new ResponseEntity<>(allWorldWonders, HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
