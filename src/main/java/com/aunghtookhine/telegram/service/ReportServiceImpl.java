package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
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
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService{
    private ReportRepository reportRepository;
    private ReportMapper reportMapper;
    private ExcelGenerator excelGenerator;

    @Override
    public ResponseEntity<ResponseMessage> createReport(ReportDto dto) {
        Report savedReport = reportRepository.save(reportMapper.toReport(dto));
        return ResponseEntity.ok().body(new ResponseMessage("Success", savedReport));
    }

    @Override
    public File exportReport(LocalDate date) throws IOException{
        List<Report> reports = reportRepository.findAllByDate(date);
        return excelGenerator.createExcelFile(date, reports);
    }
}
