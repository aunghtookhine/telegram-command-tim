package com.aunghtookhine.telegram.utils;

import com.aunghtookhine.telegram.entity.Report;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ExcelGenerator {
    public ByteArrayInputStream createExcelFile(LocalDate date, List<Report> reports) throws IOException {
        // Create a new Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report for " + date);

        // Create a header row
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
            headerRow.createCell(0).setCellValue(report.getId());
            headerRow.createCell(1).setCellValue(report.getDate());
            headerRow.createCell(2).setCellValue(report.getTitle());
            headerRow.createCell(3).setCellValue(report.getDescription());
            headerRow.createCell(4).setCellValue(report.getCreatedBy());
            headerRow.createCell(5).setCellValue(report.getCreatedAt());
            headerRow.createCell(6).setCellValue(report.getCreatedBy());
        }

//        // Adjust column width to fit content
//        for (int i = 0; i < 3; i++) {
//            sheet.autoSizeColumn(i);
//        }

        // Write the workbook to a byte array output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        // Return the workbook as a ByteArrayInputStream
        return new ByteArrayInputStream(out.toByteArray());
    }
}
