package com.skinclear.skinclearbackend.resource;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse {
    private boolean success;
    private String message;
    private Object data;
    private Error error;
    private String errorType;
}

