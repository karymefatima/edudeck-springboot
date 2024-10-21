package com.it332.edudeck.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.it332.edudeck.Entity.FlashcardDeck;
import com.it332.edudeck.Entity.Flashcard;
import com.it332.edudeck.Repository.FlashcardDeckRepository;
import com.it332.edudeck.Repository.FlashcardRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class GeminiFlashcardAI {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final FlashcardDeckRepository flashcardDeckRepository;
    private final FlashcardRepository flashcardRepository;

    public GeminiFlashcardAI(FlashcardDeckRepository flashcardDeckRepository, FlashcardRepository flashcardRepository) {
        this.flashcardDeckRepository = flashcardDeckRepository;
        this.flashcardRepository = flashcardRepository;
    }

    @PostMapping("/generate-flashcards/{deckId}")
public String generateFlashcards(@RequestBody String lessonText, @PathVariable int deckId) {
    try {
        // Define the system prompt
        String systemPrompt = "You are to create flashcard pairs (question and answer, in json format if possible) " +
                "based on the given lesson texts to help the student user review and ace his/her exams. " +
                "Design it in a way that when the user reads the question/flashcard front, they have an idea of what's the answer/flashcard back. " +
                "Make the answers/flashcard back easier to understand and even give tips to the user to immediately remember the concept.";

        // Escape special characters in the lessonText and systemPrompt
        lessonText = lessonText.replace("\n", "\\n").replace("\"", "\\\"");
        systemPrompt = systemPrompt.replace("\n", "\\n").replace("\"", "\\\"");

        // Prepare the API request body
        String requestBody = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [{ \"text\": \"" + systemPrompt + "\\n" + lessonText + "\" }]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // Create headers and set the content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Create the RestTemplate and make the API call
        RestTemplate restTemplate = new RestTemplate();
        String geminiUrl = geminiApiUrl + "?key=" + geminiApiKey;

        ResponseEntity<String> responseEntity = restTemplate.exchange(geminiUrl, HttpMethod.POST, requestEntity, String.class);
        String responseBody = responseEntity.getBody();

        // Strip any backticks from the response body
        responseBody = responseBody.replaceAll("```json|```", "").trim();

        // Check if responseBody is empty or incomplete
        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("Received empty response from the API");
        }

        // Parse the API response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode geminiData = objectMapper.readTree(responseBody);

        // Validate that the response contains the expected structure
        JsonNode candidates = geminiData.path("candidates");
        if (!candidates.isArray() || candidates.size() == 0) {
            throw new RuntimeException("Invalid or empty candidates array in the response");
        }

        // Extract the content part of the first candidate
        String apiResponse = candidates.get(0).path("content").path("parts").get(0).path("text").asText();

        // Split the API response into individual sentences (flashcard pairs)
        List<String> generatedSentences = apiResponse.lines()
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.toList());

        // Save the flashcards to the database
        saveFlashcards(generatedSentences, deckId);

        return "Flashcards generated and saved successfully.";
    } catch (Exception e) {
        e.printStackTrace();
        return "Error occurred: " + e.getMessage();
    }
}

private void saveFlashcards(List<String> generatedSentences, int deckId) throws IOException {
        FlashcardDeck flashcardDeck = flashcardDeckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
    
        ObjectMapper objectMapper = new ObjectMapper();
    
        for (String sentence : generatedSentences) {
            // Check if sentence is a valid JSON object (ignore arrays and other invalid data)
            if (sentence.trim().startsWith("{") && sentence.trim().endsWith("}")) {
                try {
                    // Parse each sentence as a JSON object
                    JsonNode flashcardNode = objectMapper.readTree(sentence);
    
                    if (flashcardNode.has("question") && flashcardNode.has("answer")) {
                        String question = flashcardNode.get("question").asText();
                        String answer = flashcardNode.get("answer").asText();
    
                        // Save the flashcard to the deck
                        Flashcard flashcard = new Flashcard(question, answer, flashcardDeck);
                        flashcardRepository.save(flashcard);
                    } else {
                        System.out.println("Invalid flashcard format: missing 'question' or 'answer'. " + sentence);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to parse flashcard: " + sentence);
                    e.printStackTrace();
                }
            } else {
                // Log invalid JSON data for further debugging
                System.out.println("Invalid JSON format for flashcard: " + sentence);
            }
        }
    }
    
}
