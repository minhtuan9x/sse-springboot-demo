package com.example.testeventsource.api;


import com.example.testeventsource.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UploadAPI {
    @Autowired
    UploadFileService uploadFileService;

    @GetMapping("/upload")
    public ResponseEntity<Void> upload(@RequestParam String uuid) {
        uploadFileService.upTest( uuid);
        return ResponseEntity.ok().build();
    }
}
