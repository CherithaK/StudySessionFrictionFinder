package com.friction.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** A dated daily study plan holding its tasks in display order. */
public class StudySession {
    private int id;
    private final LocalDate date;
    private final List<StudyTask> tasks = new ArrayList<>();

    public StudySession(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Date is required.");
        this.date = date;
    }

    public void addTask(StudyTask task) {
        tasks.add(task);
    }

    public int getTotalPlannedMinutes() {
        int total = 0;
        for (StudyTask t : tasks) total += t.getPlannedMinutes();
        return total;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public List<StudyTask> getTasks() { return tasks; }
}
