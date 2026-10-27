package com.friction;

import com.friction.db.StudySessionDAO;
import com.friction.db.StudySessionRepository;
import com.friction.report.DailyReport;
import com.friction.report.WeeklyReport;
import com.friction.service.AnalyticsService;
import com.friction.service.RecommendationEngine;
import com.friction.service.SessionHistoryService;
import com.friction.service.StudyPlanService;
import com.friction.ui.MenuController;

/** Entry point: wires the layers together and starts the menu. */
public class Main {
    public static void main(String[] args) {
        StudySessionRepository repository = new StudySessionDAO();
        AnalyticsService analytics = new AnalyticsService();
        SessionHistoryService history = new SessionHistoryService(repository);

        MenuController menu = new MenuController(
                new StudyPlanService(repository),
                history,
                new DailyReport(history, analytics),
                new WeeklyReport(history, analytics),
                new RecommendationEngine(history, analytics));
        menu.start();
    }
}
