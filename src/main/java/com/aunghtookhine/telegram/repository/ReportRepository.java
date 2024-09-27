package com.aunghtookhine.telegram.repository;

import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findAllByDateAndReportType(LocalDate date, ReportType reportType);
}
