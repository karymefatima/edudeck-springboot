package com.it332.edudeck.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.it332.edudeck.Entity.Quiz;
import com.it332.edudeck.Repository.QuizRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(int id) {
        return quizRepository.findById(id);
    }

    @SuppressWarnings("finally")
	public Quiz updateQuiz(int quizId, Quiz newQuizDetails) {
		Quiz quiz = new Quiz();
		try {
			quiz = quizRepository.findById(quizId).get();
			
            if (newQuizDetails.getTitle() != null) {
                quiz.setTitle(newQuizDetails.getTitle());
            }
            if (newQuizDetails.getTotalQuestions() != 0) {
                quiz.setTotalQuestions(newQuizDetails.getTotalQuestions());
            }
            if (newQuizDetails.getTargetScorePercentage() != 0.0f) {
                quiz.setTargetScorePercentage(newQuizDetails.getTargetScorePercentage());
            }
            if (newQuizDetails.getPassingScore() != 0) {
                quiz.setPassingScore(newQuizDetails.getPassingScore());
            }
            if (newQuizDetails.getScore() != 0) {
                quiz.setScore(newQuizDetails.getScore());
            }
            if (newQuizDetails.getScorePercentage() != 0.0f) {
                quiz.setScorePercentage(newQuizDetails.getScorePercentage());
            }
            if (newQuizDetails.getDateLastTaken() != null) {
                quiz.setDateLastTaken(newQuizDetails.getDateLastTaken());
            }
            if (newQuizDetails.getSubject() != null) {
                quiz.setSubject(newQuizDetails.getSubject());
            }
            if (newQuizDetails.getFeedback() != 0) {
                quiz.setFeedback(newQuizDetails.getFeedback());
            }

		}catch(NoSuchElementException ex) {
			throw new NoSuchElementException("Quiz "+ quizId + " does not exist!");
		}finally {
			return quizRepository.save(quiz);
		}
	}

    public List<Map<String, Object>> getAverageScoresBySubject(int userId) {
        List<Object[]> results = quizRepository.findAverageScoresBySubject(userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> subjectScore = new HashMap<>();
            subjectScore.put("subject", result[0]);
            subjectScore.put("averageScore", result[1]);
            response.add(subjectScore);
        }
        return response;
    }

    public boolean softDeleteQuiz(int id) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            quiz.setDeleted(true);
            quizRepository.save(quiz);
            return true;
        } else {
            return false;
        }
    }
}
