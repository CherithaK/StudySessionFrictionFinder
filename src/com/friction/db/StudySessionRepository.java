package com.friction.db;

import com.friction.model.StudySession;
import com.friction.model.StudyTask;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Persistence operations for study plans (abstraction over the database). */
public interface StudySessionRepository {

    /** Saves a new session and all its tasks in one transaction. Returns the new session id. */
    int save(StudySession session) throws SQLException;

    /** Returns the plan for a date, or null if none exists. */
    StudySession findByDate(LocalDate date) throws SQLException;

    /** Returns all plans between two dates (inclusive), oldest first. */
    List<StudySession> findBetween(LocalDate from, LocalDate to) throws SQLException;

    /** Saves the new status, actual minutes and note of a task. */
    void updateTask(StudyTask task) throws SQLException;
}
