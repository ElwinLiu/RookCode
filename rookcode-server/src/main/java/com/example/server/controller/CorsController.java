package com.example.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CorsController {
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity handle() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE, PATCH")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }
}
