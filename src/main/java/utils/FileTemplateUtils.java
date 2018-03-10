package utils;

import api.DataParser;
import exceptions.WrongTemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;

/**
 * Утилитный класс для работы с шаблоном
 */
public class FileTemplateUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileTemplateUtils.class);

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
            LOGGER.error("Ошибка при обращении к файлу с шаблоном", e);
            throw new WrongTemplateException(e.getMessage());
        }
    }
}
