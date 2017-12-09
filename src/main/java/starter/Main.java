package starter;

import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;
import utils.FileDataUtils;
import utils.FileTemplateUtils;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Docx doc = FileTemplateUtils.getFile();

        VariablePattern pattern = FileTemplateUtils.getPattern();
        doc.setVariablePattern(pattern);

        List<Map<String, String>> data = FileDataUtils.getData(pattern.getOriginalPrefix(), pattern.getOriginalSuffix());
        Variables var = new Variables();
        for (Map<String, String> rowValues : data) {
            for (Map.Entry<String, String> stringStringEntry : rowValues.entrySet()) {
                var.addTextVariable(new TextVariable(stringStringEntry.getKey(), stringStringEntry.getValue()));
            }

        }

// fill template by given map of variables
        doc.fillTemplate(var);

// save filled document
        doc.save("C:\\Users\\user1\\Desktop\\test\\output.docx");


    }
}
