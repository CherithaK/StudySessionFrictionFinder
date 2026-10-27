package com.friction.report;

import com.friction.model.StudySession;
import com.friction.model.StudyTask;
import com.friction.model.TaskStatus;
import com.friction.service.AnalyticsService;
import com.friction.service.SessionHistoryService;
import com.friction.util.DateUtil;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/** Summary of the Monday-to-Sunday week that contains the selected date. */
public class WeeklyReport implements ReportGenerator {
    private final SessionHistoryService historyService;
    private final AnalyticsService analytics;

    public WeeklyReport(SessionHistoryService historyService, AnalyticsService analytics) {
        this.historyService = historyService;
        this.analytics = analytics;
    }

    @Override
    public String generate(LocalDate date) throws SQLException {
        LocalDate monday = date.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        // TreeMap keeps the days sorted by date
        Map<LocalDate, List<StudyTask>> byDay = new TreeMap<>();
        List<StudyTask> all = new ArrayList<>();
        for (StudySession s : historyService.getSessions(monday, sunday)) {
            byDay.put(s.getDate(), s.getTasks());
            all.addAll(s.getTasks());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========== WEEKLY REPORT ==========\n");
        sb.append("Period: ").append(DateUtil.format(monday)).append(" to ").append(DateUtil.format(sunday)).append("\n\n");
        if (all.isEmpty()) {
            return sb.append("No study plans found for this week.").toString();
        }

        sb.append(String.format("Tasks planned        : %d%n", all.size()));
        sb.append(String.format("Completed            : %d%n", analytics.countByStatus(all).get(TaskStatus.COMPLETED)));
        sb.append(String.format("Completion rate      : %.1f%%%n%n", analytics.completionRate(all)));
        sb.append(String.format("Planned time         : %,d min%n", analytics.totalPlanned(all)));
        sb.append(String.format("Actual time recorded : %,d min%n", analytics.totalActual(all)));
        sb.append(String.format("Net overrun          : %+,d min%n", analytics.netOverrun(all)));

        sb.append("\nDay by day:\n");
        for (Map.Entry<LocalDate, List<StudyTask>> e : byDay.entrySet()) {
            List<StudyTask> tasks = e.getValue();
            sb.append(String.format("  %s  %d tasks  %5.1f%% done  %+d min%n", DateUtil.format(e.getKey()),
                    tasks.size(), analytics.completionRate(tasks), analytics.netOverrun(tasks)));
        }

        List<String> overruns = analytics.recurringOverruns(all);
        Set<String> partials = analytics.recurringPartials(all);
        List<String> friction = new ArrayList<>();
        List<String> focus = new ArrayList<>();
        for (String g : overruns) {
            friction.add(g + " frequently exceeds estimates.");
            focus.add("Allocate larger blocks for " + g + ".");
        }
        for (String g : partials) {
            friction.add(g + " is often partially completed.");
            focus.add("Break " + g + " into smaller tasks.");
        }

        sb.append("\nRecurring friction:\n");
        if (friction.isEmpty()) {
            sb.append("None detected this week.\n");
        } else {
            for (int i = 0; i < friction.size(); i++) sb.append(i + 1).append(". ").append(friction.get(i)).append("\n");
            sb.append("\nFocus for next week:\n");
            for (String f : focus) sb.append("- ").append(f).append("\n");
        }
        sb.append("===================================");
        return sb.toString();
    }
}
