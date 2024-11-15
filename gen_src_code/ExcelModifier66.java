import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier66 {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over each row and process the last column
            for (Row row : sheet) {
                Cell lastCell = row.getCell(row.getLastCellNum() - 1);
                if (lastCell != null && lastCell.getCellType() == CellType.NUMERIC) {
                    double originalValue = lastCell.getNumericCellValue();
                    double newValue = originalValue * 10;
                    lastCell.setCellValue(newValue);
                }
            }

            // Write the output to a new file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

            System.out.println("File has been modified and saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}