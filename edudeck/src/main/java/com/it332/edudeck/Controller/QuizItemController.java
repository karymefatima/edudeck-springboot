package com.it332.edudeck.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.it332.edudeck.Entity.Flashcard;
import com.it332.edudeck.Entity.QuizItem;
import com.it332.edudeck.Service.QuizItemService;

import java.util.List;

@RestController
@RequestMapping("/api/quizitems")
public class QuizItemController {

    @Autowired
    private QuizItemService quizItemService;

    @PostMapping("/create")
    public ResponseEntity<QuizItem> createQuizItem(@RequestBody QuizItem quizItem) {
        QuizItem createdQuizItem = quizItemService.createQuizItem(quizItem);
        return ResponseEntity.ok(createdQuizItem);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuizItem>> getAllQuizItems() {
        return ResponseEntity.ok(quizItemService.getAllQuizItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizItem> getQuizItemById(@PathVariable int id) {
        QuizItem quizItem = quizItemService.getQuizItemById(id)
                .orElseThrow(() -> new RuntimeException("QuizItem not found with id: " + id));
        return ResponseEntity.ok(quizItem);
    }

    @PutMapping("/softdelete/{id}")
    public ResponseEntity<Void> softDeleteQuizItem(@PathVariable int id) {
        boolean isDeleted = quizItemService.softDeleteQuizItem(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/quiz/{quizId}")
    public List<QuizItem> getQuizItemsByQuizId(@PathVariable int quizId) {
        return quizItemService.getQuizItemsByQuizId(quizId);
    }
}
