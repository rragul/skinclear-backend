package com.skinclear.skinclearbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
@Service
public class S3Service {

    @Value("${cloudfront.base-url}")
    private String cloudFrontBaseUrl;

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String filename) {
        String bucketName = "skin-clear-images";
        String key = "product/" + filename;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return cloudFrontBaseUrl + "/" + key;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
