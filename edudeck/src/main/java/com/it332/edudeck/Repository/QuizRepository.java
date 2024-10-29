package com.it332.edudeck.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.it332.edudeck.Entity.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer>{

    @Query("SELECT q.subject, AVG(q.scorePercentage) FROM Quiz q WHERE q.user.id = :userId GROUP BY q.subject")
    List<Object[]> findAverageScoresBySubject(@Param("userId") int userId);
}
