package workers;

import api.DataParser;
import exceptions.CreatingTemplateFilesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;
import utils.FileTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Поток создания файлов
 */
public class CreatingFileThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatingFileThread.class);
    private static final String TMP = "tmp";

    private int index;
    private final DataParser parser;
    private String pathToSource;
    private Map<String, String> rowValues;

    public CreatingFileThread(int index, DataParser parser, String pathToSource, Map<String, String> rowValues) {
        this.index = index;
        this.parser = parser;
        this.pathToSource = pathToSource;
        this.rowValues = rowValues;
    }

    public void run() {

        Docx doc;
        synchronized (parser) {
            doc = FileTemplateUtils.getFileTemplate(parser);
            VariablePattern pattern = FileTemplateUtils.getPattern(parser);
            doc.setVariablePattern(pattern);
        }

        Variables var = new Variables();
        for (Map.Entry<String, String> stringStringEntry : rowValues.entrySet()) {
            var.addTextVariable(new TextVariable(stringStringEntry.getKey(), stringStringEntry.getValue()));
        }
        doc.fillTemplate(var);
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(pathToSource + File.separator + TMP + index + ".docx", true);
            doc.save(writer);
        }
        catch (IOException e) {
            LOGGER.error("Ошибка при сохранении документа", e);
            throw new CreatingTemplateFilesException("Ошибка при сохранении документа: " + e.getMessage());
        } finally {
            closeStream(writer);
        }
    }

    private void closeStream(FileOutputStream writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error("Ошибка при закрытии документа", e);
            throw new CreatingTemplateFilesException("Ошибка при закрытии документа: " + e.getMessage());
        }
    }
}
