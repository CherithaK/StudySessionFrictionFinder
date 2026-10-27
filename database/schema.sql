-- Study Session Friction Finder - MySQL schema
CREATE DATABASE IF NOT EXISTS friction_finder;
USE friction_finder;

DROP TABLE IF EXISTS study_tasks;
DROP TABLE IF EXISTS study_sessions;

-- One daily plan per date
CREATE TABLE study_sessions (
    session_id   INT AUTO_INCREMENT PRIMARY KEY,
    session_date DATE NOT NULL UNIQUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Many tasks per plan
CREATE TABLE study_tasks (
    task_id         INT AUTO_INCREMENT PRIMARY KEY,
    session_id      INT NOT NULL,
    task_name       VARCHAR(100) NOT NULL,
    subject         VARCHAR(50)  NOT NULL,
    task_type       VARCHAR(50)  NOT NULL,
    planned_minutes INT NOT NULL CHECK (planned_minutes > 0),
    actual_minutes  INT NOT NULL DEFAULT 0 CHECK (actual_minutes >= 0),
    priority        VARCHAR(10)  NOT NULL,
    status          VARCHAR(10)  NOT NULL DEFAULT 'PENDING',
    note            VARCHAR(255),
    FOREIGN KEY (session_id) REFERENCES study_sessions(session_id) ON DELETE CASCADE,
    INDEX idx_session (session_id)
);
