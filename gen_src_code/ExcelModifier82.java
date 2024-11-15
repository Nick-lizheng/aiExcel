import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier82 {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output22.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over each row in the sheet
            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Get the last column cell
                Cell lastColumnCell = row.getCell(row.getLastCellNum() - 1);

                // Check if the cell is not null and not empty
                if (lastColumnCell != null && lastColumnCell.getCellType() != CellType.BLANK) {
                    double value = 0;

                    // Handle N/A values and empty strings
                    if (lastColumnCell.getCellType() == CellType.STRING) {
                        String cellValue = lastColumnCell.getStringCellValue().trim();
                        if ("N/A".equals(cellValue) || cellValue.isEmpty()) {
                            value = 0;
                        } else {
                            value = Double.parseDouble(cellValue);
                        }
                    } else if (lastColumnCell.getCellType() == CellType.NUMERIC) {
                        value = lastColumnCell.getNumericCellValue();
                    }

                    // Multiply the value by 10
                    value *= 10;

                    // Set the new value back to the cell
                    lastColumnCell.setCellType(CellType.NUMERIC);
                    lastColumnCell.setCellValue(value);
                }
            }

            // Write the changes to the output file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}