package starter;

import workers.DataParser;
import workers.TemplateFilesCreator;

public class Main {
    public static void main(String[] args){

        DataParser parser = new DataParser();
        parser.parse();
        try {
            TemplateFilesCreator.createFiles(parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
