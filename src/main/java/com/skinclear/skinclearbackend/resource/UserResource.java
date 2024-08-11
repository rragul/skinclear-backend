package com.skinclear.skinclearbackend.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResource {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String profilePicture;
    private String bio;
    private String location;
    private String skinType;
    private String skinConcerns;
    private String productPreference;
    private  boolean isAdmin;
}
