package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.enums.ReportType;
import com.aunghtookhine.telegram.mapper.ReportMapper;
import com.aunghtookhine.telegram.repository.ReportRepository;
import com.aunghtookhine.telegram.response.ResponseMessage;
import com.aunghtookhine.telegram.utils.ExcelGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService{
    private ReportRepository reportRepository;
    private ReportMapper reportMapper;
    private ExcelGenerator excelGenerator;

    @Override
    public ResponseEntity<ResponseMessage> createReport(List<ReportDto> dtos) {
        List<Report> reports = new ArrayList<>();
        for (ReportDto dto: dtos){
            reports.add(reportMapper.toReport(dto));
        }
//        Report savedReport = reportRepository.save(reportMapper.toReport(dto));
        List<Report> savedReports = reportRepository.saveAll(reports);
        return ResponseEntity.ok().body(new ResponseMessage("Success", savedReports));
    }

    @Override
    public File exportDailyReport(LocalDate date, ReportType reportType) throws IOException{
        List<Report> reports = reportRepository.findAllByDateAndReportType(date, reportType);
        return excelGenerator.createExcelFile(date, reports);
    }

}
