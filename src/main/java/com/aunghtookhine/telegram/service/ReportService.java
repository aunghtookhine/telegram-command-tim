package com.aunghtookhine.telegram.service;

import com.aunghtookhine.telegram.dto.ReportDto;
import com.aunghtookhine.telegram.enums.ReportType;
import com.aunghtookhine.telegram.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ReportService {
    ResponseEntity<ResponseMessage> createReport(List<ReportDto> dto);
    File exportDailyReport(String date, ReportType reportType) throws IOException;

    File exportMonthlyReport(String yearMonth, ReportType reportType) throws IOException;
}
