package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.resource.SkinClearResponseResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class AbstractController {
    HashMap<Integer, String> codeStatusMap = new HashMap<>();

    public AbstractController() {
        codeStatusMap.put(200, "OK");
        codeStatusMap.put(201, "Created");
        codeStatusMap.put(204, "No Content");
        codeStatusMap.put(400, "Bad Request");
        codeStatusMap.put(401, "Unauthorized");
        codeStatusMap.put(403, "Forbidden");
        codeStatusMap.put(404, "Not Found");
        codeStatusMap.put(500, "Server Error");
    }


    public ResponseEntity<Object> sendSuccessResponse(Object object) {
        return ResponseEntity.ok().body(new SkinClearResponseResource(object, codeStatusMap.get(200)));
    }

    public ResponseEntity<Object> sendErrorResponse(Object object) {
        return ResponseEntity.badRequest().body(new SkinClearResponseResource(object, codeStatusMap.get(400)));
    }

    public ResponseEntity<Object> sendSuccessResponseWithHeader(String header, Object objectHeader, Object entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(header, objectHeader.toString());
        return ResponseEntity.ok().headers(headers).body(new SkinClearResponseResource(entity, codeStatusMap.get(200)));
    }

    public ResponseEntity<Object> sendSuccessResponseWithHeaderNoContent(String header, Object objectHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(header, objectHeader.toString());
        return ResponseEntity.noContent().headers(headers).build();
    }

    public ResponseEntity<Object> handleServiceException(Exception e) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Subject", "IMI PORTAL EXCEPTION");

        SkinClearResponseResource SkinClearResponseResource = new SkinClearResponseResource();
        SkinClearResponseResource.setSuccess(false);
        SkinClearResponseResource.setErrorMessage(e.getMessage());
        SkinClearResponseResource.setRequestedURI(null);
        SkinClearResponseResource.setStatusInfo(codeStatusMap.get(500));

        return ResponseEntity.badRequest().body(SkinClearResponseResource);
    }

    public ResponseEntity<Object> sendUnAuthenticationResponse(Exception e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Subject", "Authentication Fail.");
        return ResponseEntity.badRequest().body(new SkinClearResponseResource(e.getMessage(), codeStatusMap.get(401)));
    }

}
