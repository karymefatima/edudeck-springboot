package com.it332.edudeck.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tblhighlight")
public class HighlightEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int highlightId;

    private int startPosition;
    private int endPosition;
    private String highlightType;
    private LocalDateTime dateCreated = LocalDateTime.now();
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private DocumentEntity document;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public HighlightEntity() {
    }

    public HighlightEntity(int highlightId, int startPosition, int endPosition, String highlightType,
            LocalDateTime dateCreated, boolean isDeleted, DocumentEntity document, UserEntity user) {
        this.highlightId = highlightId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.highlightType = highlightType;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        this.document = document;
        this.user = user;
    }

    public int getHighlightId() {
        return highlightId;
    }

    public void setHighlightId(int highlightId) {
        this.highlightId = highlightId;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public String getHighlightType() {
        return highlightType;
    }

    public void setHighlightType(String highlightType) {
        this.highlightType = highlightType;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public DocumentEntity getDocument() {
        return document;
    }

    public void setDocument(DocumentEntity document) {
        this.document = document;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    
}
