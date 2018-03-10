package workers;

import api.DataParser;
import exceptions.CreatingTemplateFilesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import utils.FileDataUtils;
import utils.FileTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс создания файлов на основе шаблона
 */
public class TemplateFilesCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateFilesCreator.class);
    private static final String TMP = "tmp";

    private DataParser parser;

    public TemplateFilesCreator(DataParser parser) {
        this.parser = parser;
    }

    public String createTemplateFilesAndGetSource() {
        VariablePattern pattern = FileTemplateUtils.getPattern(parser);
        List<Map<String, String>> data = FileDataUtils.getData(pattern.getOriginalPrefix(), pattern.getOriginalSuffix(), parser);
        Docx doc = FileTemplateUtils.getFileTemplate(parser);
        doc.setVariablePattern(pattern);
        int i = 0;
        String pathToSource = createDirectoryAndGetPath();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (Map<String, String> rowValues : data) {
            Runnable worker = new CreatingFileThread(i, parser, pathToSource, rowValues);
            executor.execute(worker);
            i++;
        }
        executor.shutdown();
        while (!executor.isTerminated()) {   }
        LOGGER.trace("Задачи выполнены");

        return pathToSource;
    }

    private String createDirectoryAndGetPath() {
        String pathToSource = parser.getOutputPath() + TMP;
        Path path = Paths.get(pathToSource);
        if (Files.exists(path)) {
            deleteAllFilesFromTmp(pathToSource);
            return pathToSource;
        }
        boolean mkdir = new File(pathToSource).mkdir();
        if (!mkdir) {
            LOGGER.error("Ошибка при создании директории");
            throw new CreatingTemplateFilesException("Ошибка при создании директории");
        }
        return pathToSource;
    }

    private void deleteAllFilesFromTmp(String path) {
        File[] files = new File(path).listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                LOGGER.error("Ошибка при очистке директории");
                throw new CreatingTemplateFilesException("Ошибка при очистке директории");
            }
        }
    }
}
