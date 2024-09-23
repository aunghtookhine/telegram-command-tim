package com.aunghtookhine.telegram.utils;

import com.aunghtookhine.telegram.entity.Report;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class ExcelGenerator {
    public ByteArrayInputStream createExcelFile(LocalDate date, List<Report> reports) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report ("+ date +")");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Title");
        headerRow.createCell(3).setCellValue("Description");
        headerRow.createCell(4).setCellValue("CreatedBy");
        headerRow.createCell(5).setCellValue("CreatedAt");
        headerRow.createCell(6).setCellValue("UpdatedAt");

        int rowCount = 1;
        for (Report report: reports){
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(report.getId());
            row.createCell(1).setCellValue(report.getDate().toString());
            row.createCell(2).setCellValue(report.getTitle());
            row.createCell(3).setCellValue(report.getDescription());
            row.createCell(4).setCellValue(report.getCreatedBy());
            row.createCell(5).setCellValue(report.getCreatedAt().toString());
            row.createCell(6).setCellValue(report.getUpdatedAt().toString());
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
