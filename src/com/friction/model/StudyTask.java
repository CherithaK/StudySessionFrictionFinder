package com.friction.model;

/** One to-do item inside a daily study plan. */
public class StudyTask {
    private int id;
    private int sessionId;
    private String name;
    private String subject;
    private String type;
    private int plannedMinutes;
    private int actualMinutes;
    private Priority priority;
    private TaskStatus status;
    private String note;

    /** Creates a new PENDING task (not saved yet). */
    public StudyTask(String name, String subject, String type, int plannedMinutes, Priority priority) {
        setName(name);
        setSubject(subject);
        setType(type);
        setPlannedMinutes(plannedMinutes);
        this.priority = priority;
        this.status = TaskStatus.PENDING;
        this.actualMinutes = 0;
    }

    /** Positive = took longer than planned, negative = finished early. */
    public int getOverrun() {
        return actualMinutes - plannedMinutes;
    }

    public boolean isUnfinished() {
        return status != TaskStatus.COMPLETED;
    }

    // ---------- getters / setters with validation ----------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Task name cannot be empty.");
        this.name = name.trim();
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) {
        if (subject == null || subject.isBlank()) throw new IllegalArgumentException("Subject cannot be empty.");
        this.subject = subject.trim();
    }

    public String getType() { return type; }
    public void setType(String type) {
        if (type == null || type.isBlank()) throw new IllegalArgumentException("Task type cannot be empty.");
        this.type = type.trim();
    }

    public int getPlannedMinutes() { return plannedMinutes; }
    public void setPlannedMinutes(int plannedMinutes) {
        if (plannedMinutes <= 0) throw new IllegalArgumentException("Planned duration must be positive.");
        this.plannedMinutes = plannedMinutes;
    }

    public int getActualMinutes() { return actualMinutes; }
    /** Total time spent so far (replaces the old value, so repeated updates never double-count). */
    public void setActualMinutes(int actualMinutes) {
        if (actualMinutes < 0) throw new IllegalArgumentException("Actual time cannot be negative.");
        this.actualMinutes = actualMinutes;
    }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = (note == null || note.isBlank()) ? null : note.trim(); }
}
