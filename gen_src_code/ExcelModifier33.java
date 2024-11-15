import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelModifier33 {

    public static void main(String[] args) {
        String inputFilePath = "./excel_file/latest_10min_wind.xlsx";
        String outputFilePath = "./excel_file/output.xlsx";

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastColumnIndex = sheet.getRow(0).getLastCellNum() - 1; // 获取最后一列的索引

            for (Row row : sheet) {
                Cell lastCell = row.getCell(lastColumnIndex);
                if (lastCell != null && lastCell.getCellType() == CellType.NUMERIC) {
                    double value = lastCell.getNumericCellValue();
                    double newValue = value * 10;
                    lastCell.setCellValue(newValue);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                workbook.write(fos);
            }

            System.out.println("文件处理完成，新文件已保存至: " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}