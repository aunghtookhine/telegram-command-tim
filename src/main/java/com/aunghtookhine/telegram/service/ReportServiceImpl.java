package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.mapper.ReportMapper;
import com.aunghtookhine.telegram.repository.ReportRepository;
import com.aunghtookhine.telegram.response.ResponseMessage;
//import com.aunghtookhine.telegram.utils.ExcelCreate;
import com.aunghtookhine.telegram.utils.ExcelGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService{
    private ReportRepository reportRepository;
    private ReportMapper reportMapper;
    @Override
    public ResponseEntity<ResponseMessage> createReport(ReportDto dto) {
        Report savedReport = reportRepository.save(reportMapper.toReport(dto));
        return ResponseEntity.ok().body(new ResponseMessage("Success", savedReport));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportReport(LocalDate date) throws IOException{
//        response.setContentType("application/octet-stream");
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//        String currentDateTime = dateFormat.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
//        response.setHeader(headerKey, headerValue);
//
//        List<Report> reports = reportRepository.findAllByDate(date);
//        ExcelCreate generator = new ExcelCreate(reports);
//        generator.generateExcelFIle(response);
        List<Report> reports = reportRepository.findAllByDate(date);
        ExcelGenerator excelGenerator = new ExcelGenerator();
        ByteArrayInputStream excelFile = excelGenerator.createExcelFile(date, reports);
        InputStreamResource resource = new InputStreamResource(excelFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + date + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }
}
