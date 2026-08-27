package com.hashforge.hashforge.controllers;

import com.hashforge.hashforge.Dto.LogRequest;
import com.hashforge.hashforge.model.CommitLog;
import com.hashforge.hashforge.service.CommitManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/commit")
public class CommitController {
    private final CommitManagerService commitManagerService;

    public CommitController(CommitManagerService commitManagerService) {
        this.commitManagerService = commitManagerService;
    }

    @PostMapping("/log")
    public ResponseEntity<?> logs(@RequestBody LogRequest request) throws IOException {
        String repositoryPath = request.getRepositoryPath();
        System.out.println("Repository Path = " + repositoryPath);
        List<CommitLog> logs = commitManagerService.log(repositoryPath);
        return ResponseEntity.ok(logs);

    }
}
