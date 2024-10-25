package com.it332.edudeck.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it332.edudeck.Entity.Flashcard;
import com.it332.edudeck.Entity.Quiz;
import com.it332.edudeck.Entity.QuizItem;
import com.it332.edudeck.Repository.QuizItemRepository;
import com.it332.edudeck.Repository.QuizRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizItemService {

    @Autowired
    private QuizItemRepository quizItemRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<QuizItem> getQuizItemsByQuizId(int quizId) {
        return quizItemRepository.findByQuiz_QuizId(quizId).stream()
        .filter(quizItem -> !quizItem.isDeleted())
        .collect(Collectors.toList());
    }

    public QuizItem createQuizItem(String question, List<String> options, String correctAnswer, String questionType, String userAnswer, int quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        QuizItem quizItem = new QuizItem();
        quizItem.setQuestion(question);
        quizItem.setOptions(options);
        quizItem.setCorrectAnswer(correctAnswer);
        quizItem.setQuestionType(questionType);
        quizItem.setUserAnswer(userAnswer);
        quizItem.setQuiz(quiz);

        return quizItemRepository.save(quizItem);
    }

    public List<QuizItem> getAllQuizItems() {
        return quizItemRepository.findAll();
    }

    public Optional<QuizItem> getQuizItemById(int id) {
        return quizItemRepository.findById(id);
    }

    public boolean softDeleteQuizItem(int id) {
        Optional<QuizItem> quizItemOptional = quizItemRepository.findById(id);
        if (quizItemOptional.isPresent()) {
            QuizItem quizItem = quizItemOptional.get();
            quizItem.setDeleted(true);
            quizItemRepository.save(quizItem);
            return true;
        } else {
            return false;
        }
    }
}

