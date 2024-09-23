package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.response.ResponseMessage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;

public interface ReportService {
    ResponseEntity<ResponseMessage> createReport(ReportDto dto);
    ResponseEntity<InputStreamResource> exportReport(LocalDate date) throws IOException;
}
