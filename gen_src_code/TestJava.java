import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestJava {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over the rows starting from the second row (index 1)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell meanSpeedCell = row.getCell(3);
                    Cell maxGustCell = row.getCell(4);

                    if (meanSpeedCell != null && maxGustCell != null) {
                        double meanSpeed = meanSpeedCell.getNumericCellValue();
                        double maxGust = maxGustCell.getNumericCellValue();

                        // Create a new cell in column F
                        Cell resultCell = row.createCell(5);
                        resultCell.setCellValue(meanSpeed + " km/h, " + maxGust + " km/h");
                    }
                }
            }

            // Write the output to a new file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

            System.out.println("Excel file processed and saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}