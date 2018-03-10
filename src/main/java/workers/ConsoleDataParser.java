package workers;

import api.DataParser;

import java.io.File;
import java.util.Scanner;

/**
 * Консольный парсер данных
 */
public class ConsoleDataParser implements DataParser {
    private String templatePath;
    private String inputPath;
    private String outputPath;
    private String leftPartPattern;
    private String rightPartPattern;

    public String getTemplatePath() {
        return templatePath;
    }

    private void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getInputPath() {
        return inputPath;
    }

    private void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    private void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getLeftPartPattern() {
        return leftPartPattern;
    }

    private void setLeftPartPattern(String leftPartPattern) {
        this.leftPartPattern = leftPartPattern;
    }

    public String getRightPartPattern() {
        return rightPartPattern;
    }

    private void setRightPartPattern(String rightPartPattern) {
        this.rightPartPattern = rightPartPattern;
    }

    public void parse(){
        setTemplatePath(parseFileTemplatePath());
        setInputPath(parseInputFilePath());
        setOutputPath(getPath(this.inputPath));
        setLeftPartPattern(parseLeftPartPattern());
        setRightPartPattern(parseRightPartPattern());
    }

    private String getPath(String path) {
        int i = path.lastIndexOf(File.separator);
        return path.substring(0, i + 1);
    }

    private String parseFileTemplatePath(){
        Scanner in = new Scanner(System.in);
        System.out.println("Введите полный путь до файла с шаблоном");
        return in.nextLine();
    }

    private String parseInputFilePath() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите полный путь до файла с данными");
        return in.nextLine();
    }

    private String parseLeftPartPattern() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите левую часть паттерна");
        return in.nextLine();
    }

    private String parseRightPartPattern() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите правую часть паттерна");
        return in.nextLine();
    }
}
