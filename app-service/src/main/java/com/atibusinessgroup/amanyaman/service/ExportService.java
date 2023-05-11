package com.atibusinessgroup.amanyaman.service;

// import com.atibusinessgroup.amanyaman.domain.Product;
import com.atibusinessgroup.amanyaman.domain.User;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class ExportService {

    public Resource exportUsers(List<User> userList) {
        Workbook workbook = new XSSFWorkbook();

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Users");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        // style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        // font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("First Name");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Last Name");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Email");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Role");
        header.getCell(3).setCellStyle(style);
        // header.createCell(4).setCellValue("Travel Agent Name");
        // header.getCell(4).setCellStyle(style);
        int rowCount = 1;

        for (User u : userList) {
            Row userRow = sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(u.getFirstName());
            userRow.createCell(1).setCellValue(u.getLastName());
            userRow.createCell(2).setCellValue(u.getEmail());
            if(u.getAuthorities() != null && u.getAuthorities().size() > 0) {
                userRow.createCell(3).setCellValue(u.getAuthorities().stream().findFirst().get().getName());
            }
            // if(u.getTravelAgent() != null){
            //     userRow.createCell(4).setCellValue(u.getTravelAgent().getTravelAgentName());
            // }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(out.toByteArray());
    }

    public Resource exportProduct(List<Product> productList){
        Workbook workbook = new XSSFWorkbook();

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Products");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        // style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        // font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        int i = 0;
        Row header = sheet.createRow(i);
        header.createCell(i).setCellValue("Product ID");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Product Code");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Product Detail Code");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Currency");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Plan Type");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Product Description");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Product Accident Cover");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Modical Cover");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Travel Cover");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Product Type");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Travel Duration");
        header.getCell(i).setCellStyle(style);
        i++;
        header.createCell(i).setCellValue("Additional Week");
        header.getCell(i).setCellStyle(style);
        i++;
//        header.createCell(i).setCellValue("Updated Date");
//        header.getCell(i).setCellStyle(style);

        int rowCount = 1;

        for (Product u : productList) {
            Row userRow = sheet.createRow(rowCount++);
            // userRow.createCell(0).setCellValue(u.getId());
            // userRow.createCell(1).setCellValue(u.getProductCode());
            // userRow.createCell(2).setCellValue(u.getProductDtlCode());
            // userRow.createCell(3).setCellValue(u.getCurrId());
            // userRow.createCell(4).setCellValue(u.getPlanTypeNum());
            // userRow.createCell(5).setCellValue(u.getProductDescription());
            // userRow.createCell(6).setCellValue(u.getProductPersonalAccidentCover());
            // userRow.createCell(7).setCellValue(u.getProductMedicalCover());
            // userRow.createCell(8).setCellValue(u.getProductTravelCover());
            // userRow.createCell(9).setCellValue(u.getProductType() != null ? u.getProductType().getProductTypeName() : "");
            // userRow.createCell(10).setCellValue(u.getTravelDuration() != null ? u.getTravelDuration().getTravelDurationName() : "");
            // userRow.createCell(11).setCellValue(u.getProductAdditionalWeek() != null ? u.getProductAdditionalWeek().getProductCode() : "");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(out.toByteArray());
    }
}
