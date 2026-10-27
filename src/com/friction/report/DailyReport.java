package com.friction.report;

import com.friction.model.StudySession;
import com.friction.model.StudyTask;
import com.friction.model.TaskStatus;
import com.friction.service.AnalyticsService;
import com.friction.service.SessionHistoryService;
import com.friction.util.DateUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Summary of a single day. */
public class DailyReport implements ReportGenerator {
    private final SessionHistoryService historyService;
    private final AnalyticsService analytics;

    public DailyReport(SessionHistoryService historyService, AnalyticsService analytics) {
        this.historyService = historyService;
        this.analytics = analytics;
    }

    @Override
    public String generate(LocalDate date) throws SQLException {
        StudySession session = historyService.getSession(date);
        if (session == null || session.getTasks().isEmpty()) {
            return "No study plan found for " + DateUtil.format(date) + ".";
        }
        List<StudyTask> tasks = session.getTasks();
        Map<TaskStatus, Integer> counts = analytics.countByStatus(tasks);

        StringBuilder sb = new StringBuilder();
        sb.append("========== DAILY REPORT ==========\n");
        sb.append("Date: ").append(DateUtil.format(date)).append("\n\n");
        sb.append(String.format("Tasks planned        : %d%n", tasks.size()));
        sb.append(String.format("Completed            : %d%n", counts.get(TaskStatus.COMPLETED)));
        sb.append(String.format("Partial              : %d%n", counts.get(TaskStatus.PARTIAL)));
        sb.append(String.format("Skipped              : %d%n", counts.get(TaskStatus.SKIPPED)));
        sb.append(String.format("Pending              : %d%n", counts.get(TaskStatus.PENDING)));
        sb.append(String.format("Completion rate      : %.1f%%%n%n", analytics.completionRate(tasks)));
        sb.append(String.format("Planned study time   : %,d min%n", analytics.totalPlanned(tasks)));
        sb.append(String.format("Actual time recorded : %,d min%n", analytics.totalActual(tasks)));
        sb.append(String.format("Net overrun          : %+d min%n", analytics.netOverrun(tasks)));

        List<StudyTask> over = analytics.tasksExceedingEstimate(tasks);
        if (!over.isEmpty()) {
            sb.append("\nTasks exceeding estimate:\n");
            for (StudyTask t : over) {
                sb.append(String.format("- %s (%s): +%d min%n", t.getName(), AnalyticsService.groupKey(t), t.getOverrun()));
            }
            sb.append("\nSuggestion:\nReserve a longer block for ")
              .append(AnalyticsService.groupKey(over.get(0))).append(".\n");
        }
        sb.append("==================================");
        return sb.toString();
    }
}
