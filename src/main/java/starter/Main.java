package starter;

import api.DataParser;
import workers.TemplateFilesCreator;
import workers.ConsoleDataParser;

public class Main {
    public static void main(String[] args) throws Exception {
        DataParser parser = new ConsoleDataParser();
        parser.parse();
        TemplateFilesCreator creator = new TemplateFilesCreator(parser);
        String source = creator.createTemplateFilesAndGetSource();
        System.out.println("Файлы находятся в " + source);
    }
}
