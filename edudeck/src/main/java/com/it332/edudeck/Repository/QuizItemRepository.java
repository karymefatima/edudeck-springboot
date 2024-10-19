package com.it332.edudeck.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.it332.edudeck.Entity.QuizItem;

public interface QuizItemRepository extends JpaRepository<QuizItem, Integer>{
    List<QuizItem> findByQuiz_QuizId(int quizId);
}
