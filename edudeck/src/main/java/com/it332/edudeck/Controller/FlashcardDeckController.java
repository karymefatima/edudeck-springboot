package com.it332.edudeck.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.it332.edudeck.Entity.FlashcardDeck;
import com.it332.edudeck.Service.FlashcardDeckService;
import com.it332.edudeck.Service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/decks")
public class FlashcardDeckController {

    @Autowired
    private FlashcardDeckService flashcardDeckService;

    @Autowired
    private UserService userService;

    @PostMapping("/createFlashcardDeck")
    public ResponseEntity<FlashcardDeck> createFlashcardDeck(@RequestBody FlashcardDeck flashcardDeck) {
        FlashcardDeck createdDeck = flashcardDeckService.createFlashcardDeck(flashcardDeck.getTitle(), flashcardDeck.getUser(), flashcardDeck.getDocument());
        return ResponseEntity.ok(createdDeck);
    }

    // New method for manual deck creation
    @PostMapping("/createManualDeck")
    public ResponseEntity<FlashcardDeck> createManualDeck(@RequestBody FlashcardDeck flashcardDeck) {
        // Ensure no document is attached to manually created decks
        if (flashcardDeck.getDocument() != null) {
            return ResponseEntity.badRequest().body(null);
        }

        FlashcardDeck createdDeck = flashcardDeckService.createFlashcardDeck(
            flashcardDeck.getTitle(), 
            flashcardDeck.getUser(), 
            null // Explicitly pass null for document
        );
        return ResponseEntity.ok(createdDeck);
    }

    @GetMapping("/getAllFlashcardDecks")
    public ResponseEntity<List<FlashcardDeck>> getAllFlashcardDecks() {
        return ResponseEntity.ok(flashcardDeckService.getAllFlashcardDecks());
    }

    @GetMapping("/getFlashcardDeckById/{id}")
    public ResponseEntity<FlashcardDeck> getFlashcardDeckById(@PathVariable int id) {
        Optional<FlashcardDeck> flashcardDeck = flashcardDeckService.getFlashcardDeckById(id);
        return flashcardDeck.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getDecksByUser/{userId}")
    public List<FlashcardDeck> getDecksByUser(@PathVariable int userId) {
        return flashcardDeckService.getDecksByUser(userId);
    }

    // U - Update a FlashcardDeck record
	@PutMapping("/updateFlashcardDeck")
	public FlashcardDeck updateDeck(@RequestParam int deckid, @RequestBody FlashcardDeck newFlashcardDeckDetails) {
		return flashcardDeckService.updateDeck(deckid, newFlashcardDeckDetails);
	}

    //soft delete
	@PutMapping("/deleteFlashcardDeck/{deckId}")
	public String deleteDeck(@PathVariable int deckId) {
	    return flashcardDeckService.deleteFlashcardDeck(deckId);
	}
}
