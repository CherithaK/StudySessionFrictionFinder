-- Sample history for the last few days (dates are relative to today,
-- so the demo always has data). Run after schema.sql.
USE friction_finder;

INSERT INTO study_sessions (session_id, session_date) VALUES
(1, CURDATE() - INTERVAL 6 DAY),
(2, CURDATE() - INTERVAL 5 DAY),
(3, CURDATE() - INTERVAL 3 DAY),
(4, CURDATE() - INTERVAL 2 DAY),
(5, CURDATE() - INTERVAL 1 DAY);

INSERT INTO study_tasks (session_id, task_name, subject, task_type, planned_minutes, actual_minutes, priority, status, note) VALUES
(1, 'Solve tree problems',   'DSA',  'Practice',   90, 115, 'HIGH',   'COMPLETED', NULL),
(1, 'OS assignment 1',       'OS',   'Assignment', 60,  40, 'MEDIUM', 'PARTIAL',   'Got stuck on Q3'),
(2, 'Solve graph problems',  'DSA',  'Practice',   90, 120, 'HIGH',   'COMPLETED', NULL),
(2, 'Revise ER diagrams',    'DBMS', 'Revision',   45,  40, 'LOW',    'COMPLETED', NULL),
(3, 'Solve DP problems',     'DSA',  'Practice',   60,  85, 'HIGH',   'COMPLETED', NULL),
(3, 'OS assignment 2',       'OS',   'Assignment', 60,  30, 'MEDIUM', 'PARTIAL',   NULL),
(4, 'Practice SQL queries',  'DBMS', 'Practice',   45,   0, 'MEDIUM', 'SKIPPED',   NULL),
(4, 'Read CN chapter 3',     'CN',   'Reading',    30,  30, 'LOW',    'COMPLETED', NULL),
(5, 'Revise normalization',  'DBMS', 'Revision',   45,  25, 'MEDIUM', 'PARTIAL',   'Need more practice'),
(5, 'Solve heap problems',   'DSA',  'Practice',   60,  80, 'HIGH',   'COMPLETED', NULL);
