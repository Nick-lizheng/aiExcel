import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier27 {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output3.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Define new column headers
            String[] newColumns = {"Wind Speed Category", "Gust Speed Category", "Average Wind Speed"};

            // Add new column headers to the first row
            Row headerRow = sheet.getRow(0);
            int lastColumnIndex = headerRow.getLastCellNum();
            for (int i = 0; i < newColumns.length; i++) {
                Cell cell = headerRow.createCell(lastColumnIndex + i);
                cell.setCellValue(newColumns[i]);
            }

            // Iterate over each row in the sheet
            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Get the mean speed and maximum gust cells
                Cell meanSpeedCell = row.getCell(3);
                Cell maxGustCell = row.getCell(4);

                // Initialize values
                double meanSpeed = 0;
                double maxGust = 0;

                // Handle N/A values and empty strings
                if (meanSpeedCell != null && meanSpeedCell.getCellType() == CellType.NUMERIC) {
                    meanSpeed = meanSpeedCell.getNumericCellValue();
                }

                if (maxGustCell != null && maxGustCell.getCellType() == CellType.NUMERIC) {
                    maxGust = maxGustCell.getNumericCellValue();
                }

                // Create new columns
                Cell windSpeedCategoryCell = row.createCell(lastColumnIndex);
                Cell gustSpeedCategoryCell = row.createCell(lastColumnIndex + 1);
                Cell averageWindSpeedCell = row.createCell(lastColumnIndex + 2);

                // Wind Speed Category
                if (meanSpeed < 10) {
                    windSpeedCategoryCell.setCellValue("Low");
                } else if (meanSpeed >= 10 && meanSpeed < 20) {
                    windSpeedCategoryCell.setCellValue("Moderate");
                } else {
                    windSpeedCategoryCell.setCellValue("High");
                }

                // Gust Speed Category
                if (maxGust < 20) {
                    gustSpeedCategoryCell.setCellValue("Low");
                } else if (maxGust >= 20 && maxGust < 40) {
                    gustSpeedCategoryCell.setCellValue("Moderate");
                } else {
                    gustSpeedCategoryCell.setCellValue("High");
                }

                // Average Wind Speed
                averageWindSpeedCell.setCellType(CellType.NUMERIC);
                averageWindSpeedCell.setCellValue((meanSpeed + maxGust) / 2);
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