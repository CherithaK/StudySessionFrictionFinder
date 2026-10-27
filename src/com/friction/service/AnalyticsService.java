package com.friction.service;

import com.friction.model.StudyTask;
import com.friction.model.TaskStatus;

import java.util.*;

/**
 * Shared calculations used by reports and recommendations.
 * Completion rate = completed / all planned tasks x 100 (skipped tasks are counted).
 */
public class AnalyticsService {

    /** Number of tasks for every status. EnumMap keeps the enum order. */
    public Map<TaskStatus, Integer> countByStatus(List<StudyTask> tasks) {
        Map<TaskStatus, Integer> counts = new EnumMap<>(TaskStatus.class);
        for (TaskStatus s : TaskStatus.values()) counts.put(s, 0);
        for (StudyTask t : tasks) counts.put(t.getStatus(), counts.get(t.getStatus()) + 1);
        return counts;
    }

    public double completionRate(List<StudyTask> tasks) {
        if (tasks.isEmpty()) return 0;
        return countByStatus(tasks).get(TaskStatus.COMPLETED) * 100.0 / tasks.size();
    }

    public int totalPlanned(List<StudyTask> tasks) {
        int sum = 0;
        for (StudyTask t : tasks) sum += t.getPlannedMinutes();
        return sum;
    }

    public int totalActual(List<StudyTask> tasks) {
        int sum = 0;
        for (StudyTask t : tasks) sum += t.getActualMinutes();
        return sum;
    }

    /**
     * Net overrun = actual - planned for completed tasks, plus partial tasks that already
     * went over. Unfinished time is not counted as "saved" time.
     */
    public int netOverrun(List<StudyTask> tasks) {
        int sum = 0;
        for (StudyTask t : tasks) {
            if (t.getStatus() == TaskStatus.COMPLETED) {
                sum += t.getOverrun();
            } else if (t.getStatus() == TaskStatus.PARTIAL && t.getOverrun() > 0) {
                sum += t.getOverrun();
            }
        }
        return sum;
    }

    /** Tasks that took longer than planned, biggest overrun first (PriorityQueue ranking). */
    public List<StudyTask> tasksExceedingEstimate(List<StudyTask> tasks) {
        PriorityQueue<StudyTask> queue =
                new PriorityQueue<>((a, b) -> Integer.compare(b.getOverrun(), a.getOverrun()));
        for (StudyTask t : tasks) {
            if (t.getOverrun() > 0) queue.add(t);
        }
        List<StudyTask> result = new ArrayList<>();
        while (!queue.isEmpty()) result.add(queue.poll());
        return result;
    }

    /** Groups tasks by "Subject Type", e.g. "DSA Practice" (HashMap aggregation). */
    public Map<String, List<StudyTask>> groupBySubjectAndType(List<StudyTask> tasks) {
        Map<String, List<StudyTask>> groups = new HashMap<>();
        for (StudyTask t : tasks) {
            groups.computeIfAbsent(groupKey(t), k -> new ArrayList<>()).add(t);
        }
        return groups;
    }

    /** Average actual/planned ratio for completed tasks of a group (1.0 = on time). */
    public double averageRatio(List<StudyTask> tasks) {
        int planned = 0, actual = 0;
        for (StudyTask t : tasks) {
            if (t.getStatus() == TaskStatus.COMPLETED) {
                planned += t.getPlannedMinutes();
                actual += t.getActualMinutes();
            }
        }
        return planned == 0 ? 1.0 : (double) actual / planned;
    }

    /** Groups that went over estimate at least twice, worst total overrun first. */
    public List<String> recurringOverruns(List<StudyTask> tasks) {
        List<String> result = new ArrayList<>();
        PriorityQueue<Map.Entry<String, Integer>> queue =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        for (Map.Entry<String, List<StudyTask>> e : groupBySubjectAndType(tasks).entrySet()) {
            int count = 0, total = 0;
            for (StudyTask t : e.getValue()) {
                if (t.getOverrun() > 0) {
                    count++;
                    total += t.getOverrun();
                }
            }
            if (count >= 2) queue.add(Map.entry(e.getKey(), total));
        }
        while (!queue.isEmpty()) result.add(queue.poll().getKey());
        return result;
    }

    /** Groups that ended PARTIAL at least twice. TreeSet keeps them sorted alphabetically. */
    public Set<String> recurringPartials(List<StudyTask> tasks) {
        Set<String> result = new TreeSet<>();
        for (Map.Entry<String, List<StudyTask>> e : groupBySubjectAndType(tasks).entrySet()) {
            int partials = 0;
            for (StudyTask t : e.getValue()) {
                if (t.getStatus() == TaskStatus.PARTIAL) partials++;
            }
            if (partials >= 2) result.add(e.getKey());
        }
        return result;
    }

    public static String groupKey(StudyTask t) {
        return t.getSubject() + " " + t.getType();
    }
}
