package com.aunghtookhine.telegram.mapper;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
import org.springframework.stereotype.Service;

@Service
public class ReportMapper {
    public Report toReport(ReportDto dto){
        Report report = new Report();
        report.setTitle(dto.getTitle());
        report.setDate(dto.getDate());
        report.setDescription(dto.getDescription());
        report.setCreatedBy(dto.getCreatedBy());
        return report;
    }
    public ReportDto toReportDto(Report report){
        return new ReportDto(report.getTitle(), report.getDescription(), report.getDate(), report.getCreatedBy());
    }
}
