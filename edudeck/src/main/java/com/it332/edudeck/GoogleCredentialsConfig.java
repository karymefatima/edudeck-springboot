package com.it332.edudeck;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class GoogleCredentialsConfig {

    @Bean
    public GoogleCredentials googleCredentials() throws Exception {
        // Load the JSON credentials from the environment variable
        String googleCredentialsJson = System.getenv("GOOGLE_CREDENTIALS");

        // Check if the environment variable is null or empty
        if (googleCredentialsJson == null || googleCredentialsJson.isEmpty()) {
            throw new IllegalArgumentException("GOOGLE_CREDENTIALS environment variable is not set or is empty");
        }

        // Parse the JSON string into a Map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> credentialsMap = objectMapper.readValue(googleCredentialsJson, Map.class);

        // Replace escaped newlines in the private key
        String privateKey = (String) credentialsMap.get("private_key");
        privateKey = privateKey.replace("\\n", "\n");
        credentialsMap.put("private_key", privateKey);

        // Convert the credentials Map back to a JSON string and create GoogleCredentials
        String credentialsString = objectMapper.writeValueAsString(credentialsMap);
        ByteArrayInputStream credentialsStream = new ByteArrayInputStream(credentialsString.getBytes(StandardCharsets.UTF_8));

        return ServiceAccountCredentials.fromStream(credentialsStream);
    }
}
