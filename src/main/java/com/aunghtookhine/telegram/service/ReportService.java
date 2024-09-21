package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.response.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;

public interface ReportService {
    ResponseEntity<ResponseMessage> createReport(ReportDto dto);

    void exportReport(HttpServletResponse response, LocalDate date) throws IOException;
}
