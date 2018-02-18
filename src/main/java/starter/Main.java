package starter;

import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;
import workers.DataParser;
import utils.FileDataUtils;
import utils.FileTemplateUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args){

        DataParser parser = new DataParser();
        parser.parse();
        Docx doc = FileTemplateUtils.getFileTemplate(parser);

        VariablePattern pattern = FileTemplateUtils.getPattern(parser);
        doc.setVariablePattern(pattern);


        List<Map<String, String>> data = FileDataUtils.getData(pattern.getOriginalPrefix(), pattern.getOriginalSuffix(), parser);
        Variables var = new Variables();
        for (Map<String, String> rowValues : data) {
            for (Map.Entry<String, String> stringStringEntry : rowValues.entrySet()) {
                var.addTextVariable(new TextVariable(stringStringEntry.getKey(), stringStringEntry.getValue()));
            }
            doc.fillTemplate(var);
            try (        FileOutputStream writer = new FileOutputStream(parser.getOutputPath(), true)) {
                doc.save(writer);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
