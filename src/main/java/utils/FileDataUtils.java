package utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by user1 on 09.12.2017.
 */
public class FileDataUtils {

    public static List<Map<String, String>> getData(String prefix, String suffix) {
        String path = getPathToFile();

        List<Map<String, String>> data = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow rowHeader = sheet.getRow(0);
            short lastCellNum = rowHeader.getLastCellNum();
            LinkedList<String> keys = new LinkedList<>();
            for (int i = 0 ; i < lastCellNum; i++) {
                String rawValue = rowHeader.getCell(i).getStringCellValue();
                keys.add(prefix + rawValue + suffix);
            }
            int lastRowNum = sheet.getLastRowNum();

            for (int k = 1; k < lastRowNum + 1; k++) {
                XSSFRow row = sheet.getRow(k);
                Map<String, String> values = new HashMap<>();
                for (int j = 0; j < lastCellNum; j++) {
                    values.put(keys.get(j), row.getCell(j).getStringCellValue());
                }
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String getPathToFile() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите полный путь до файла с данными");
        return in.nextLine();
    }
}
