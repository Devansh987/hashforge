package com.hashforge.hashforge.controllers;

import com.hashforge.hashforge.Dto.InitRepositoryRequest;
import com.hashforge.hashforge.Dto.RepositoryStatusRequest;
import com.hashforge.hashforge.Dto.RepositoryStatusResponse;
import com.hashforge.hashforge.model.RepositoryStatus;
import com.hashforge.hashforge.service.RepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/repository")
public class RepositoryController {

    private RepositoryService repositoryService;
    public RepositoryController(RepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }


    @PostMapping("/init")
    public ResponseEntity<?> createRepo(@RequestBody InitRepositoryRequest request){

            RepositoryStatus status = repositoryService.initRepository(request.getPath());
        return switch (status) {
            case CREATED -> new ResponseEntity<>(
                    status,
                    HttpStatus.CREATED
            );
            case ALREADY_EXISTS -> new ResponseEntity<>(
                    status,
                    HttpStatus.CONFLICT
            );
            case PROJECT_NOT_FOUND -> new ResponseEntity<>(
                    status,
                    HttpStatus.NOT_FOUND
            );
            case NOT_A_DIRECTORY -> new ResponseEntity<>(
                    status,
                    HttpStatus.BAD_REQUEST
            );
            default -> new ResponseEntity<>(
                    status,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        };
    }

    @PostMapping("/status")
    public ResponseEntity<?> status(@RequestBody RepositoryStatusRequest repositoryStatusRequest) throws IOException, NoSuchAlgorithmException {

        RepositoryStatusResponse response = repositoryService
                .getStatus(repositoryStatusRequest.getRepositoryPath());
        return ResponseEntity.ok(response);
    }
}
