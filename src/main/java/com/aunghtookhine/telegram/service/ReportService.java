package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.response.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;

public interface ReportService {
    ResponseEntity<ResponseMessage> createReport(ReportDto dto);

    ResponseEntity<InputStreamResource> exportReport(LocalDate date) throws IOException;
}
