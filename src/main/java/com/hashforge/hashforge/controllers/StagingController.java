package com.hashforge.hashforge.controllers;


import com.hashforge.hashforge.Dto.AddRequest;
import com.hashforge.hashforge.service.StagingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RequestMapping("/add")
@RestController
public class StagingController {

    private final StagingService stagingService;

    public StagingController(StagingService stagingService) {
        this.stagingService = stagingService;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody AddRequest addRequest) throws IOException, NoSuchAlgorithmException {
        stagingService.stageFiles(addRequest.getFilePath(), addRequest.getRepositoryPath());
        return ResponseEntity.ok("File staged successfully");
    }

}
