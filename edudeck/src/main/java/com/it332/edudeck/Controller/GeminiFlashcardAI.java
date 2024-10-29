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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

@RestController
public class GeminiFlashcardAI {

    @Autowired
    private FlashcardDeckRepository flashcardDeckRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @PostMapping("/generate-flashcards/{deckId}")
    public String generateFlashcards(@RequestBody String lessonText, @PathVariable int deckId) {
        VertexAI vertexAi = null;
        try {
            vertexAi = new VertexAI("savvy-depot-423506-j9", "us-central1");

            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder()
                            .setMaxOutputTokens(8192)
                            .setTemperature(1F)
                            .setTopP(0.95F)
                            .build();
            List<SafetySetting> safetySettings = Arrays.asList(
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build()
            );

            String systemInstructionText = "You are to create flashcard pairs (question and answer, in json format if possible) " +
                    "based on the given lesson texts to help the student user review and ace his/her exams. " +
                    "design it in a way that when the user reads the question/flashcardfront, s/he has idea of whats the answer/flashcardback. " +
                    " the use of the flashcards is to help the user understand and not memorize the lesson." +
                    "make the answers/flashcardback easier to understand and even give tips to the user to immediately remember the concept.";

            Content systemInstruction = ContentMaker.fromMultiModalData(systemInstructionText);

            GenerativeModel model = new GenerativeModel.Builder()
                    .setModelName("gemini-1.5-flash-001")
                    .setVertexAi(vertexAi)
                    .setGenerationConfig(generationConfig)
                    .setSafetySettings(safetySettings)
                    .setSystemInstruction(systemInstruction)
                    .build();

            Content content = ContentMaker.fromMultiModalData(lessonText);
            ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream(content);

            // Collect and concatenate the text parts, ensuring it starts and ends with square brackets
            String result = responseStream.stream()
                    .flatMap(response -> response.getCandidatesList().stream())
                    .flatMap(candidate -> candidate.getContent().getPartsList().stream())
                    .map(Part::getText)
                    .collect(Collectors.joining());

            // Remove enclosing backticks
            result = result.replaceAll("```json|```", "").trim();

            // Save the flashcards to the database
            saveFlashcards(result, deckId);

            return "Flashcards generated and saved successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        } finally {
            if (vertexAi != null) {
                vertexAi.close();
            }
        }
    }

    private void saveFlashcards(String jsonResponse, int deckId) throws IOException {
        FlashcardDeck flashcardDeck = flashcardDeckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode flashcardsNode = objectMapper.readTree(jsonResponse);

        for (Iterator<JsonNode> it = flashcardsNode.elements(); it.hasNext(); ) {
            JsonNode flashcardNode = it.next();
            String question = flashcardNode.get("question").asText();
            String answer = flashcardNode.get("answer").asText();

            Flashcard flashcard = new Flashcard(question, answer, flashcardDeck);
            flashcardRepository.save(flashcard);
        }
    }
}