import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier70 {
    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastColumnIndex = sheet.getRow(0).getLastCellNum() - 1; // 获取最后一列的索引

            for (Row row : sheet) {
                Cell gustCell = row.getCell(lastColumnIndex);
                if (gustCell != null && gustCell.getCellType() == CellType.NUMERIC) {
                    double gustValue = gustCell.getNumericCellValue();
                    gustValue *= 10;
                    gustCell.setCellValue(gustValue);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}