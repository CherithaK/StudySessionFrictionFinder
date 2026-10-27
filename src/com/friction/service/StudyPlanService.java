package com.friction.service;

import com.friction.db.StudySessionRepository;
import com.friction.model.StudySession;
import com.friction.model.StudyTask;
import com.friction.model.TaskStatus;

import java.sql.SQLException;
import java.time.LocalDate;

/** Creates daily plans and updates task progress. */
public class StudyPlanService {
    private final StudySessionRepository repository;

    public StudyPlanService(StudySessionRepository repository) {
        this.repository = repository;
    }

    public boolean planExists(LocalDate date) throws SQLException {
        return repository.findByDate(date) != null;
    }

    /** Saves a new plan. Refuses duplicates so an existing plan is never overwritten. */
    public void createPlan(StudySession session) throws SQLException {
        if (session.getTasks().isEmpty()) {
            throw new IllegalArgumentException("A plan needs at least one task.");
        }
        if (planExists(session.getDate())) {
            throw new IllegalArgumentException("A plan already exists for " + session.getDate() + ".");
        }
        repository.save(session);
    }

    /** Updates a task. actualMinutes is the TOTAL time spent so far, not an increment. */
    public void updateTask(StudyTask task, TaskStatus status, int actualMinutes, String note) throws SQLException {
        task.setStatus(status);
        task.setActualMinutes(actualMinutes);
        if (note != null && !note.isBlank()) {
            task.setNote(note);
        }
        repository.updateTask(task);
    }
}
