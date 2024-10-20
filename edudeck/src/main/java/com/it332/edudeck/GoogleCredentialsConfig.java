package com.it332.edudeck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

@Configuration
public class GoogleCredentialsConfig {

    @Value("${google.application.credentials}")
    private String credentials;

    @PostConstruct
    public void init() throws Exception {
        // Get the base64 string from the environment variable
        String base64Credentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_BASE64");

        if (base64Credentials != null) {
            // Decode the base64 string
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);

            // Write the credentials to a temporary file
            File tempFile = new File(credentials);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(decodedBytes);
            }

            // Set the environment variable for Google APIs to use this file
            System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", tempFile.getAbsolutePath());
        }
    }
}
