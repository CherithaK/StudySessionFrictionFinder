# Study Session Friction Finder

A menu-driven Java command-line app to plan daily study tasks, update their progress,
look back at past sessions, compare planned vs actual time, generate daily/weekly reports
and get a recommended plan based on your history.

**Tech:** Java 17+ · OOP · Java Collections · JDBC · MySQL

```
========================================
      STUDY SESSION FRICTION FINDER
========================================
  1. Create Study Plan
  2. Session History
  3. Update Session
  4. Daily Report
  5. Weekly Report
  6. Recommended Plan
  0. Exit
========================================
```

## Project structure (packages)

```
src/com/friction/
├── Main.java                     entry point – wires everything together
├── model/                        plain data classes (encapsulation)
│   ├── StudySession.java         a dated daily plan + its tasks (ArrayList)
│   ├── StudyTask.java            one task: name, subject, type, planned/actual minutes, status...
│   ├── TaskStatus.java           enum: PENDING, COMPLETED, PARTIAL, SKIPPED
│   └── Priority.java             enum: HIGH, MEDIUM, LOW
├── db/                           database layer (all SQL lives here)
│   ├── DBConnection.java         opens MySQL connections
│   ├── StudySessionRepository.java   interface (abstraction)
│   └── StudySessionDAO.java      JDBC implementation (PreparedStatement, transactions)
├── service/                      business logic
│   ├── StudyPlanService.java     create plans, update tasks, block duplicate plans
│   ├── SessionHistoryService.java    read plans by date / date range
│   ├── AnalyticsService.java     completion rate, overrun, recurring friction
│   └── RecommendationEngine.java builds an explainable plan from history
├── report/                       reports (polymorphism)
│   ├── ReportGenerator.java      interface
│   ├── DailyReport.java
│   └── WeeklyReport.java         Monday–Sunday week
├── ui/                           command-line interface
│   ├── MenuController.java       menu + screens (no SQL, no calculations)
│   └── InputHelper.java          validated keyboard input
└── util/
    └── DateUtil.java             DD-MM-YYYY parsing/formatting
database/
├── schema.sql                    creates the database and tables
└── sample_data.sql               sample history for the last 6 days
```

Flow: `User → ui (MenuController) → service / report → db (DAO) → MySQL`

## Setup

1. **MySQL** – create the database and (optionally) load sample data:
   ```bash
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/sample_data.sql
   ```
2. **JDBC driver** – download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   (Platform Independent) and copy the `mysql-connector-j-x.x.x.jar` file into `lib/`.
3. **Database login** – defaults are `root` with an empty password on
   `jdbc:mysql://localhost:3306/friction_finder`. To change them, set environment variables:
   ```bash
   export DB_USER=root
   export DB_PASSWORD=yourpassword
   ```
4. **Run**
   ```bash
   ./run.sh          # macOS / Linux
   run.bat           # Windows
   ```

## How it works

| Feature | Details |
|---|---|
| Create Study Plan | Date + one or more tasks. Every task starts as `PENDING`. Only one plan per date. |
| Session History | Pick a date, see every task with planned/actual time and status. |
| Update Session | Pick a task, set status, **total** time spent so far and a note. Re-updating replaces the time, so nothing is double-counted. |
| Daily Report | Counts per status, completion rate, planned vs actual time, tasks that ran over. |
| Weekly Report | Same for the Monday–Sunday week, plus a day-by-day table and recurring friction (a subject+type that ran over or stayed partial at least twice). |
| Recommended Plan | Carries forward unfinished tasks from the last 7 days and adds extra time for subject+types that usually run over. Each suggestion shows its reason. You can accept, edit or discard – an existing plan is never overwritten. |

**Metrics**

- Completion rate = completed tasks ÷ all planned tasks × 100 (skipped tasks count in the total)
- Overrun = actual − planned minutes (positive = took longer)
- Net overrun = sum of overruns for completed tasks (+ partial tasks that already went over)

## OOP and Collections used

| Concept | Where |
|---|---|
| Encapsulation | private fields + validated setters in `StudyTask`, `StudySession` |
| Abstraction | `StudySessionRepository` interface hides JDBC from the services |
| Polymorphism | `ReportGenerator` interface → `DailyReport`, `WeeklyReport` |
| `ArrayList` | tasks of a session, in display order |
| `HashMap` | group tasks by subject + type (`AnalyticsService`) |
| `LinkedHashMap` | group SQL rows into sessions in date order (`StudySessionDAO`) |
| `TreeMap` | weekly day-by-day stats sorted by date (`WeeklyReport`) |
| `PriorityQueue` | rank tasks / groups with the biggest overruns (`AnalyticsService`) |
| `EnumMap` | count tasks per `TaskStatus` |
| `TreeSet` | sorted set of groups that are often left partial |

## Demo sequence

1. Load `sample_data.sql`.
2. **Create Study Plan** for today with three tasks.
3. **Update Session** – mark one `COMPLETED`, one `PARTIAL`.
4. **Session History** for today.
5. **Daily Report**, then **Weekly Report**.
6. **Recommended Plan** for tomorrow → accept.
