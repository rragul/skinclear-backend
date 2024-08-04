package com.skinclear.skinclearbackend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.skinclear.skinclearbackend.dto.RegisterDTO;
import com.skinclear.skinclearbackend.entity.User;
import com.skinclear.skinclearbackend.repository.UserRepository;
import com.skinclear.skinclearbackend.resource.UserResource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public UserService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }


    public void checkEligibility(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void register(RegisterDTO user, MultipartFile image) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("User already exists");
                });

        User newUser = new User();
        if (image != null) {
            String fileName = user.getUserName() + "." + image.getOriginalFilename().split("\\.")[1];
            String imageUrl = s3Service.uploadFile(image,fileName, "user");
            newUser.setProfilePicture(imageUrl);
        }
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setLocation(user.getLocation());
        newUser.setBio(user.getBio());
        newUser.setSkinType(user.getSkinType());
        newUser.setSkinConcerns(user.getSkinConcerns());
        newUser.setProductPreference(user.getProductPreference());
        newUser.setAdmin(false);
        userRepository.save(newUser);
    }

    public UserResource getUser(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResource userResource = new UserResource();
        userResource.setBio(user.getBio());
        userResource.setEmail(user.getEmail());
        userResource.setFirstName(user.getFirstName());
        userResource.setLastName(user.getLastName());
        userResource.setUserName(user.getUserName());
        userResource.setLocation(user.getLocation());
        userResource.setProfilePicture(user.getProfilePicture());
        userResource.setSkinConcerns(user.getSkinConcerns());
        userResource.setSkinType(user.getSkinType());
        userResource.setProductPreference(user.getProductPreference());
        return userResource;
    }

    @Transactional
    public void updateUser(Principal principal, RegisterDTO user, MultipartFile image) {
        String email = principal.getName();
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (image != null) {
            String fileName = existingUser.getUserName() + "." + image.getOriginalFilename().split("\\.")[1];
            String imageUrl = s3Service.uploadFile(image,fileName, "user");
            existingUser.setProfilePicture(imageUrl);
        }
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setLocation(user.getLocation());
        existingUser.setBio(user.getBio());
        existingUser.setSkinType(user.getSkinType());
        existingUser.setSkinConcerns(user.getSkinConcerns());
        existingUser.setProductPreference(user.getProductPreference());
        userRepository.save(existingUser);
    }
}
