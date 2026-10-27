package com.friction.report;

import java.sql.SQLException;
import java.time.LocalDate;

/** Common interface for reports (polymorphism: daily and weekly reports). */
public interface ReportGenerator {
    String generate(LocalDate date) throws SQLException;
}
