package com.it332.edudeck.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tblquizitem")
public class QuizItem {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quizItemId;

    private String question;
    @ElementCollection
    private List<String> options;
    private String correctAnswer;
    private String userAnswer;
    private boolean isDeleted;
    private String questionType;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonBackReference // Prevent infinite recursion
    private Quiz quiz;

    @ManyToMany
    @JoinTable(
        name = "quizitem_flashcard",
        joinColumns = @JoinColumn(name = "quiz_item_id"),
        inverseJoinColumns = @JoinColumn(name = "flashcard_id")
    )
    private List<Flashcard> flashcards;

    public QuizItem() {
    }

    

    public QuizItem(int quizItemId, String question, List<String> options, String correctAnswer, String questionType,
                    String userAnswer, boolean isDeleted, Quiz quiz, List<Flashcard> flashcards) {
        this.quizItemId = quizItemId;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType;
        this.userAnswer = userAnswer;
        this.isDeleted = isDeleted;
        this.quiz = quiz;
        this.flashcards = flashcards;
    }



    public int getQuizItemId() {
        return quizItemId;
    }

    public void setQuizItemId(int quizItemId) {
        this.quizItemId = quizItemId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }


    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    
}
