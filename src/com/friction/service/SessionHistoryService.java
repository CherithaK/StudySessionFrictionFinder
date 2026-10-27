package com.friction.service;

import com.friction.db.StudySessionRepository;
import com.friction.model.StudySession;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Read-only access to saved plans. */
public class SessionHistoryService {
    private final StudySessionRepository repository;

    public SessionHistoryService(StudySessionRepository repository) {
        this.repository = repository;
    }

    /** Returns the plan for a date, or null if there is none. */
    public StudySession getSession(LocalDate date) throws SQLException {
        return repository.findByDate(date);
    }

    public List<StudySession> getSessions(LocalDate from, LocalDate to) throws SQLException {
        return repository.findBetween(from, to);
    }
}
