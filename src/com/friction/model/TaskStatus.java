package com.friction.model;

/** Status of a study task. */
public enum TaskStatus {
    PENDING,    // not started / not yet completed
    COMPLETED,  // finished
    PARTIAL,    // started but unfinished
    SKIPPED     // not attempted
}
