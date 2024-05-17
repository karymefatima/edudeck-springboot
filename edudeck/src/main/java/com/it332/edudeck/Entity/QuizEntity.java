package com.it332.edudeck.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tblquiz")
public class QuizEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int quizId;

    private String title;
    private int totalQuestions;
    private float targetScorePercentage;
    private int passingScore;
    private int score;
    private float scorePercentage;
    private Date dateCreated;
    private Date dateLastTaken;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "deck_id", nullable = false)
    private FlashcardDeckEntity deck;

    @OneToMany(mappedBy = "quiz")
    private List<QuizItemEntity> quizItems;

    public QuizEntity() {
    }

    public QuizEntity(int quizId, String title, int totalQuestions, float targetScorePercentage, int passingScore,
            int score, float scorePercentage, Date dateCreated, Date dateLastTaken, boolean isDeleted,
            FlashcardDeckEntity deck, List<QuizItemEntity> quizItems) {
        this.quizId = quizId;
        this.title = title;
        this.totalQuestions = totalQuestions;
        this.targetScorePercentage = targetScorePercentage;
        this.passingScore = passingScore;
        this.score = score;
        this.scorePercentage = scorePercentage;
        this.dateCreated = dateCreated;
        this.dateLastTaken = dateLastTaken;
        this.isDeleted = isDeleted;
        this.deck = deck;
        this.quizItems = quizItems;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public float getTargetScorePercentage() {
        return targetScorePercentage;
    }

    public void setTargetScorePercentage(float targetScorePercentage) {
        this.targetScorePercentage = targetScorePercentage;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(float scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastTaken() {
        return dateLastTaken;
    }

    public void setDateLastTaken(Date dateLastTaken) {
        this.dateLastTaken = dateLastTaken;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public FlashcardDeckEntity getDeck() {
        return deck;
    }

    public void setDeck(FlashcardDeckEntity deck) {
        this.deck = deck;
    }

    public List<QuizItemEntity> getQuizItems() {
        return quizItems;
    }

    public void setQuizItems(List<QuizItemEntity> quizItems) {
        this.quizItems = quizItems;
    }

    
}
