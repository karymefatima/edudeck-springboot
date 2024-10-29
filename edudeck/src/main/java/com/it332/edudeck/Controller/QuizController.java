package com.it332.edudeck.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.it332.edudeck.Entity.Quiz;
import com.it332.edudeck.Service.QuizService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz createdQuiz = quizService.createQuiz(quiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable int id) {
        Quiz quiz = quizService.getQuizById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        return ResponseEntity.ok(quiz);
    }

    @PutMapping("/update/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable int quizId, @RequestBody Quiz newQuizDetails) {
		return ResponseEntity.ok(quizService.updateQuiz(quizId, newQuizDetails));
	}

    @GetMapping("/average-scores/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAverageScoresBySubject(@PathVariable int userId) {
        List<Map<String, Object>> averageScores = quizService.getAverageScoresBySubject(userId);
        return ResponseEntity.ok(averageScores);
    }

    @PutMapping("/softdelete/{id}")
    public ResponseEntity<Void> softDeleteQuiz(@PathVariable int id) {
        boolean isDeleted = quizService.softDeleteQuiz(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
