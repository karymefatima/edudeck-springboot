package com.it332.edudeck.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.it332.edudeck.Entity.Flashcard;
import com.it332.edudeck.Service.FlashcardService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @PostMapping("/createFlashcard/{deckId}")
    public ResponseEntity<Flashcard> createFlashcardForDeck(@PathVariable int deckId, @RequestBody Flashcard flashcard) {
        Flashcard createdFlashcard = flashcardService.createFlashcard(flashcard.getQuestion(), flashcard.getAnswer(), deckId);
        return ResponseEntity.ok(createdFlashcard);
    }

    @PutMapping("/editFlashcard/{id}")
    public ResponseEntity<Flashcard> updateFlashcard(@PathVariable int id, @RequestBody Flashcard flashcard) {
        Flashcard updatedFlashcard = flashcardService.updateFlashcard(id, flashcard.getQuestion(), flashcard.getAnswer());
        return ResponseEntity.ok(updatedFlashcard);
    }

    @GetMapping("/deck/{deckId}")
    public List<Flashcard> getAllFlashcardsByDeckId(@PathVariable int deckId) {
        return flashcardService.getAllFlashcardsByDeckId(deckId);
    }

    @GetMapping("/getAllFlashcards")
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/getFlashcardById/{id}")
    public ResponseEntity<Flashcard> getFlashcardById(@PathVariable int id) {
        Optional<Flashcard> flashcard = flashcardService.getFlashcardById(id);
        return flashcard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //soft delete
	@PutMapping("/deleteFlashcard/{id}")
	public String deleteFlashcard(@PathVariable int id) {
	    return flashcardService.deleteFlashcard(id);
	}
}
