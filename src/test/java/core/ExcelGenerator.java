package core;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;

public class ExcelGenerator {
    public static void main(String[] args) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Cabeçalho
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("USERNAME");
        header.createCell(2).setCellValue("EMAIL");
        header.createCell(3).setCellValue("PASSWORD");

        // 20 linhas
        for (int i = 1; i <= 20; i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue("usuario" + String.format("%02d", i));
            row.createCell(2).setCellValue("usuario" + String.format("%02d", i) + "@email.com");
            row.createCell(3).setCellValue("123456");
        }

        try (FileOutputStream fos = new FileOutputStream("src/test/resources/usuarios.xlsx")) {
            workbook.write(fos);
        }
        workbook.close();
        System.out.println("Planilha gerada com sucesso!");
    }
}

