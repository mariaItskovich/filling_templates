import com.sun.deploy.util.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File template = new File("C:\\Users\\user1\\Desktop\\test\\Beydzhik_proba.docx");

        Docx doc = new Docx("C:\\Users\\user1\\Desktop\\test\\Beydzhik_proba.docx");

        doc.setVariablePattern(new VariablePattern("{", "}"));

        String content = doc.readTextContent();

        System.out.println(content);

        List<String> findVariables = doc.findVariables();

        for (String var : findVariables) {
            System.out.println("VARIABLE => " + var);
        }

        Variables var = new Variables();
        var.addTextVariable(new TextVariable("{Фамилия1}", "Ыскакыскакыскакыскак"));
        var.addTextVariable(new TextVariable("{Имя1}", "Тимур"));

// fill template by given map of variables
        doc.fillTemplate(var);

// save filled document
        doc.save("C:\\Users\\user1\\Desktop\\test\\output.docx");


    }
}
