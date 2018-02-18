package utils;

import exceptions.WrongTemplateException;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import workers.DataParser;

/**
 * Created by user1 on 09.12.2017.
 */
public class FileTemplateUtils {

    public static Docx getFileTemplate(DataParser parser) {
        return getFileByPath(parser.getTemplatePath());
    }

    public static VariablePattern getPattern(DataParser parser) {
        String leftPart = parser.getLeftPartPattern();
        String rightPart = parser.getRightPartPattern();
        return new VariablePattern(leftPart, rightPart);
    }

    private static Docx getFileByPath(String path) {
        try {
            return new Docx(path);
        }
        catch (Exception e) {
            System.out.println("Ошибка " + e.getMessage());
            throw new WrongTemplateException(e.getMessage());
        }
    }
}
