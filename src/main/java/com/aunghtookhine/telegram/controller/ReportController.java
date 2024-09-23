package com.aunghtookhine.telegram.controller;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.response.ResponseMessage;
import com.aunghtookhine.telegram.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
    public ResponseEntity<InputStreamResource> exportReport(@RequestParam("date")LocalDate date) throws IOException {
        return reportService.exportReport(date);
    }
}
