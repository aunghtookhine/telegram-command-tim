//package com.aunghtookhine.telegram.utils;
//
//import com.aunghtookhine.telegram.entity.Report;
//import jakarta.servlet.ServletOutputStream;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.List;
//
//public class ExcelCreate {
//    private final List<Report> reports;
//    private final XSSFWorkbook xssfWorkbook;
//    private final XSSFSheet xssfSheet;
//
//    public ExcelCreate(List<Report> reports) {
//        this.reports = reports;
//        this.xssfWorkbook = new XSSFWorkbook();
//        this.xssfSheet = xssfWorkbook.createSheet("report");
//    }
//
//    private void writeHeader(){
//        Row row = xssfSheet.createRow(0);
//        CellStyle style = xssfWorkbook.createCellStyle();
//        XSSFFont font = xssfWorkbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(16);
//        style.setFont(font);
//        createCell(row, 0, "ID", style);
//        createCell(row, 1, "Date", style);
//        createCell(row, 2, "Title", style);
//        createCell(row, 3, "Description", style);
//        createCell(row, 4, "CreatedBy", style);
//        createCell(row, 5, "CreatedAt", style);
//        createCell(row, 6, "UpdatedAt", style);
//    }
//
//    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style){
//        xssfSheet.autoSizeColumn(columnCount);
//        Cell cell = row.createCell(columnCount);
//        if(valueOfCell instanceof String){
//            cell.setCellValue((String) valueOfCell);
//        }else if (valueOfCell instanceof LocalDate){
//            cell.setCellValue((LocalDate) valueOfCell);
//        }
//        cell.setCellStyle(style);
//    }
//
//    private void write(){
//        int rowCount = 1;
//        CellStyle style = xssfWorkbook.createCellStyle();
//        XSSFFont font = xssfWorkbook.createFont();
//        font.setFontHeight(14);
//        style.setFont(font);
//        for (Report report: reports){
//            Row row = xssfSheet.createRow(rowCount++);
//            int columnCount = 0;
//            createCell(row, columnCount++, report.getId(), style);
//            createCell(row, columnCount++, report.getDate(), style);
//            createCell(row, columnCount++, report.getTitle(), style);
//            createCell(row, columnCount++, report.getDescription(), style);
//            createCell(row, columnCount++, report.getCreatedBy(), style);
//            createCell(row, columnCount++, report.getCreatedAt(), style);
//            createCell(row, columnCount++, report.getUpdatedAt(), style);
//        }
//    }
//
//    public void generateExcelFIle(HttpServletResponse response) throws IOException {
//        writeHeader();
//        write();
//        ServletOutputStream outputStream = response.getOutputStream();
//        xssfWorkbook.write(outputStream);
//        xssfWorkbook.close();
//        outputStream.close();
//    }
//
//}
