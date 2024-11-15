import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier46 {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell gustCell = row.getCell(4); // 10-Minute Maximum Gust column (index 4)

                if (gustCell != null && gustCell.getCellType() == CellType.STRING) {
                    String gustValue = gustCell.getStringCellValue().trim();
                    if (!gustValue.equalsIgnoreCase("N/A") && !gustValue.equalsIgnoreCase("Calm")) {
                        try {
                            double gust = Double.parseDouble(gustValue);
                            gust *= 10;
                            gustCell.setCellType(CellType.NUMERIC);
                            gustCell.setCellValue(gust);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format in row " + row.getRowNum() + ": " + gustValue);
                        }
                    } else {
                        gustCell.setCellType(CellType.STRING);
                        gustCell.setCellValue(gustValue);
                    }
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