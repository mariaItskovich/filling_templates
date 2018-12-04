package utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import workers.DataParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Класс для работы с входными данными
 */
public class FileDataUtils {

    /**
     * Метод возвращает список замен, которые надо подставить в шаболон вместо паттерна
     * Мапа: ключ - значение паттерна, значение - то, чем заменить паттерн
     */
    public static List<ImmutablePair<String, Map<String, String>>> getData(String prefix, String suffix, DataParser parser){
        String path = parser.getInputPath();

        List<ImmutablePair<String, Map<String, String>>> data = new ArrayList<>();

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
                StringBuilder builder = new StringBuilder();
                XSSFRow row = sheet.getRow(k);
                Map<String, String> values = new HashMap<>();
                for (int j = 0; j < lastCellNum; j++) {
                    String secondNameKey = keys.get(j);
                    XSSFCell cell = row.getCell(j);
                    String stringCellValue = cell == null ? "" : cell.getStringCellValue();
                    if ("{Фамилия1}".equals(secondNameKey) || "{Фамилия2}".equals(secondNameKey)) {
                        builder.append(stringCellValue).append(";");
                    }
                    values.put(secondNameKey, stringCellValue);
                }
                data.add(new ImmutablePair<>(builder.toString(), values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
