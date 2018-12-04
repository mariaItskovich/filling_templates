package workers;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import exceptions.CreatingFilesException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;
import utils.FileDataUtils;
import utils.FileTemplateUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by user1 on 01.09.2018.
 */
public class TemplateFilesCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateFilesCreator.class);

    public static void createFiles(DataParser parser) throws Exception {
        VariablePattern pattern = FileTemplateUtils.getPattern(parser);
        List<ImmutablePair<String, Map<String, String>>> data = FileDataUtils.getData(pattern.getOriginalPrefix(), pattern.getOriginalSuffix(), parser);

        int outerIndex = 0;
        for (ImmutablePair<String, Map<String, String>> rowValues : data) {
            int innerIndex = 0;
            List<Docx> docs = FileTemplateUtils.getFileTemplate(parser);
            for (Docx doc : docs) {
                Variables var = new Variables();
                doc.setVariablePattern(pattern);
                for (Map.Entry<String, String> stringStringEntry : rowValues.getRight().entrySet()) {
                    var.addTextVariable(new TextVariable(stringStringEntry.getKey(), stringStringEntry.getValue()));
                }
                doc.fillTemplate(var);
                String nameOfDirectory = getNameOfDirectory(parser, outerIndex, rowValues.getLeft());
                File file = createAndGetFile(nameOfDirectory, innerIndex);
                FileOutputStream writer = null;
                try {
                    writer = new FileOutputStream(file);
                    doc.save(writer);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeStream(writer);
                }
//                if (innerIndex == 1) {
//                    convert(file, createPDFVersionAndGetFile(nameOfDirectory), nameOfDirectory);
//                }
                innerIndex++;
            }
            outerIndex++;
        }
    }

    private static File createAndGetFile(String pathname, int innerIndex) {
        File file = new File(pathname + File.separator + innerIndex + ".docx");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Ошибка создания файла", e);
                throw new CreatingFilesException("Ошибка создания файла: " + e.getMessage());
            }
        }
        return file;
    }

    private static String getNameOfDirectory(DataParser parser, int outerIndex, String secondName) {
        String pathname = parser.getOutputPath() + File.separator + outerIndex + "_" + secondName;
        File dir = new File(pathname);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return pathname;
    }

    private static void closeStream(FileOutputStream writer) {
        try {
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Ошибка при закрытии потока", e);
            throw new CreatingFilesException("Ошибка при закрытии потока: " + e.getMessage());
        }
    }

    private static void convertToPdf(File from, File to, String nameOfDirectory) {
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(nameOfDirectory))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();
        Future<Boolean> conversion = converter
                .convert(from).as(DocumentType.MS_WORD)
                .to(to).as(DocumentType.PDF)
                .prioritizeWith(1000) // optional
                .schedule();
    }

    private static void convert(File from, File to, String nameOfDirectory) throws Exception {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        InputStream in = new BufferedInputStream(new FileInputStream(from));
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(nameOfDirectory))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();

        Future<Boolean> conversion = converter
                .convert(in).as(DocumentType.DOCX)
                .to(bo).as(DocumentType.PDF)
                .prioritizeWith(1000) // optional
                .schedule();
        conversion.get();
        try (OutputStream outputStream = new FileOutputStream(to)) {
            bo.writeTo(outputStream);
        }
        in.close();
        bo.close();
    }

    private static File createPDFVersionAndGetFile(String nameOfDirectory) {
        String pdfName = nameOfDirectory + File.separator + "file.pdf";
        File pdf = new File(pdfName);
        if (!pdf.exists()) {
            try {
                pdf.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Ошибка создания файла", e);
                throw new CreatingFilesException("Ошибка создания файла: " + e.getMessage());
            }
        }
        return pdf;
    }
}