import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) continue;

                Cell gustCell = row.getCell(4); // 10-Minute Maximum Gust(km/hour) is in column E (index 4)
                if (gustCell != null && gustCell.getCellType() == CellType.NUMERIC) {
                    double originalValue = gustCell.getNumericCellValue();
                    double newValue = originalValue * 10;
                    gustCell.setCellValue(newValue);
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