package com.friction.model;

/** Priority of a study task. HIGH sorts first. */
public enum Priority {
    HIGH, MEDIUM, LOW;

    /** Converts user text like "high" or "High" into a Priority. */
    public static Priority fromText(String text) {
        return Priority.valueOf(text.trim().toUpperCase());
    }
}
