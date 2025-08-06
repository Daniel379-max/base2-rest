package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    private Sheet sheet;

    public ExcelUtils(String excelPath, String sheetName) throws IOException {
        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = new XSSFWorkbook(fis);
        this.sheet = workbook.getSheet(sheetName);
    }

    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    public String getCellData(int row, int col) {
        Row r = sheet.getRow(row);
        if (r == null) return "";
        Cell cell = r.getCell(col);
        return cell != null ? cell.toString() : "";
    }
}
