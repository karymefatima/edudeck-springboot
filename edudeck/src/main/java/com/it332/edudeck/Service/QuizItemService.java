package com.it332.edudeck.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it332.edudeck.Entity.Flashcard;
import com.it332.edudeck.Entity.QuizItem;
import com.it332.edudeck.Repository.QuizItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizItemService {

    @Autowired
    private QuizItemRepository quizItemRepository;

    public List<QuizItem> getQuizItemsByQuizId(int quizId) {
        return quizItemRepository.findByQuiz_QuizId(quizId).stream()
        .filter(quizItem -> !quizItem.isDeleted())
        .collect(Collectors.toList());
    }

    public QuizItem createQuizItem(QuizItem quizItem) {
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

