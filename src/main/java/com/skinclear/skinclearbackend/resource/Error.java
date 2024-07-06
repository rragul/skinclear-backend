package com.skinclear.skinclearbackend.resource;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private String message;
    private int code;
}
