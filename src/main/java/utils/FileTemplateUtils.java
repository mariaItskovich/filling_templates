package utils;

import exceptions.WrongTemplateException;
import org.apache.commons.lang3.StringUtils;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import workers.DataParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 09.12.2017.
 */
public class FileTemplateUtils {

    public static List<Docx> getFileTemplate(DataParser parser) {
        return getFileByPath(parser.getTemplatePath());
    }

    public static VariablePattern getPattern(DataParser parser) {
        String leftPart = parser.getLeftPartPattern();
        String rightPart = parser.getRightPartPattern();
        return new VariablePattern(leftPart, rightPart);
    }

    private static List<Docx> getFileByPath(String path) {
        try {
            String[] split = StringUtils.split(path, ";");
            List<Docx> list = new ArrayList<>();
            for (String s : split) {
                list.add(new Docx(s));
            }
            return list;
        }
        catch (Exception e) {
            System.out.println("Ошибка " + e.getMessage());
            throw new WrongTemplateException(e.getMessage());
        }
    }
}
