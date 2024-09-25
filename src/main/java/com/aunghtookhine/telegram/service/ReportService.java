package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.response.ResponseMessage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    ResponseEntity<ResponseMessage> createReport(ReportDto dto);
    File exportReport(LocalDate date) throws IOException;
}
