package com.aunghtookhine.telegram.repository;

import com.aunghtookhine.telegram.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    @Query(value = "SELECT * FROM report r WHERE r.date = :date AND r.report_type = :reportType", nativeQuery = true)
    List<Report> findAllByDateAndReportType(@Param("date") String date, @Param("reportType") String reportType);

    @Query(value = "SELECT * FROM report r WHERE r.date LIKE :yearMonth% AND r.report_type = :reportType ORDER BY r.date", nativeQuery = true)
    List<Report> findAllByDateLikeAndReportType(@Param("yearMonth") String yearMonth, @Param("reportType") String reportType);
}
