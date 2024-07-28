package com.skinclear.skinclearbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;


@Configuration
public class FirebaseConfig {
    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.admin.sdk}")
    private String firebaseAdminSdk;

    @PostConstruct
    public FirebaseApp firebaseApp() {
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(firebaseAdminSdk);
            if (resourceAsStream != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(resourceAsStream))
                        .build();

                return FirebaseApp.initializeApp(options);
            } else {
                log.error("null resourceAsStream has been found");
                return null;
            }
        } catch (IOException e) {
            log.error("firebase-admin sdk json not found in resource path");
            return null;
        }
    }
}
