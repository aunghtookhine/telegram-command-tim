package com.aunghtookhine.telegram.utils;

import com.aunghtookhine.telegram.config.AppConfig;
import com.aunghtookhine.telegram.entity.Report;
import com.aunghtookhine.telegram.enums.ReportType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.List;

@Component
@Slf4j
public class ExcelGenerator {
    private final AppConfig appConfig;

    public ExcelGenerator(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public File createExcelFile(String date, List<Report> reports, ReportType reportType) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report ("+ date +")");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Title");
        headerRow.createCell(3).setCellValue("Description");
        headerRow.createCell(4).setCellValue("Report Type");
        headerRow.createCell(5).setCellValue("CreatedBy");
        headerRow.createCell(6).setCellValue("CreatedAt");
        headerRow.createCell(7).setCellValue("UpdatedAt");

        int rowCount = 1;
        for (Report report: reports){
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(report.getId());
            row.createCell(1).setCellValue(report.getDate().toString());
            row.createCell(2).setCellValue(report.getTitle());
            row.createCell(3).setCellValue(report.getDescription());
            row.createCell(4).setCellValue(report.getReportType().toString());
            row.createCell(5).setCellValue(report.getCreatedBy());
            row.createCell(6).setCellValue(report.getCreatedAt().toString());
            row.createCell(7).setCellValue(report.getUpdatedAt().toString());
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File(appConfig.getResourceDir(), String.format("%s-%s.xlsx",date, reportType));
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            log.error("Error occurred while creating excel file: {}", e.getMessage());
        }
        workbook.close();
        return file;
    }
}

