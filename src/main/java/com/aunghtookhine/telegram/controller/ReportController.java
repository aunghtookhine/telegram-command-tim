package com.aunghtookhine.telegram.controller;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
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
    public ResponseEntity<ResponseMessage> createReport(@RequestBody ReportDto dto){
        return reportService.createReport(dto);
    }

    @GetMapping
    public File exportReport(@RequestParam("date")LocalDate date) throws IOException {
        return reportService.exportReport(date);
    }

}
