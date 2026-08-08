package com.hashforge.hashforge.controllers;

import com.hashforge.hashforge.Dto.InitRepositoryRequest;
import com.hashforge.hashforge.model.RepositoryStatus;
import com.hashforge.hashforge.service.RepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
