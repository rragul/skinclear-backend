package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.service.S3Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://d2azwxcl0602xy.cloudfront.net")
@RequestMapping("/api/v1/test")
public class TestController {

    private final S3Service s3Service;

    public TestController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file")  MultipartFile file) {
        String filename = UUID.randomUUID() + "." + file.getOriginalFilename().split("\\.")[1];
        return s3Service.uploadFile(file, filename, "test");
    }

    @GetMapping
    public String test() {
        return "Server is running...";
    }
}
