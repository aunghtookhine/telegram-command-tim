package com.aunghtookhine.telegram.controller;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.enums.ReportType;
import com.aunghtookhine.telegram.response.ResponseMessage;
import com.aunghtookhine.telegram.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class ReportController {
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createReport(@RequestBody List<ReportDto> dto){
        return reportService.createReport(dto);
    }

    @GetMapping("/daily")
    public File exportDailyReport(
            @RequestParam(value = "date")LocalDate date,
            @RequestParam(value = "type")ReportType reportType) throws IOException {
        return reportService.exportDailyReport(date, reportType);
    }

//    @GetMapping("/monthly")
//    public File exportMonthlyReport(
//            @RequestParam(value = "month")Integer month,
//            @RequestParam(value = "type")ReportType reportType) throws IOException {
//        return reportService.exportMonthlyReport(date, reportType);
//    }
}
