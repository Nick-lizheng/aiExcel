import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelModifier72 {
    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<List<String>> data = new ArrayList<>();

            // Read the header row
            Row headerRow = sheet.getRow(0);
            List<String> header = new ArrayList<>();
            for (Cell cell : headerRow) {
                header.add(cell.getStringCellValue());
            }
            data.add(header);

            // Read the data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    List<String> rowData = new ArrayList<>();
                    for (int j = 0; j < header.size(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            rowData.add("");
                        } else {
                            switch (cell.getCellType()) {
                                case STRING:
                                    rowData.add(cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        rowData.add(cell.getDateCellValue().toString());
                                    } else {
                                        rowData.add(String.valueOf(cell.getNumericCellValue()));
                                    }
                                    break;
                                default:
                                    rowData.add("");
                                    break;
                            }
                        }
                    }
                    // Add the row data twice
                    data.add(new ArrayList<>(rowData));
                    data.add(new ArrayList<>(rowData));
                }
            }

            // Create a new workbook and sheet to write the data
            Workbook newWorkbook = new XSSFWorkbook();
            Sheet newSheet = newWorkbook.createSheet("latest_10min_wind");

            // Write the header row
            Row newRow = newSheet.createRow(0);
            for (int i = 0; i < header.size(); i++) {
                Cell newCell = newRow.createCell(i);
                newCell.setCellValue(header.get(i));
            }

            // Write the data rows
            int rowIndex = 1;
            for (List<String> rowData : data) {
                Row newRowData = newSheet.createRow(rowIndex++);
                for (int j = 0; j < rowData.size(); j++) {
                    Cell newCell = newRowData.createCell(j);
                    newCell.setCellValue(rowData.get(j));
                }
            }

            // Save the new workbook to the output file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                newWorkbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}