package utils;

import api.DataParser;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Класс для работы с входными данными
 */
public class FileDataUtils {

    /**
     * Метод возвращает список замен, которые надо подставить в шаболо вместо паттерна
     * Мапа: ключ - значение паттерна, значение - то, чем заменить паттерн
     */
    public static List<Map<String, String>> getData(String prefix, String suffix, DataParser parser) {
        String path = parser.getInputPath();

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
}
