package utils;

import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;

import java.util.Scanner;

/**
 * Created by user1 on 09.12.2017.
 */
public class FileTemplateUtils {

    public static Docx getFile() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите полный путь до файла с шаблоном");
        String path = in.nextLine();
        return getFileByPath(path);
    }

    public static VariablePattern getPattern() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите левую часть паттерна");
        String leftPart = in.nextLine();
        System.out.println("Введите правую часть паттерна");
        String rightPart = in.nextLine();
        return new VariablePattern(leftPart, rightPart);
    }

    private static Docx getFileByPath(String path) {
        return new Docx(path);
    }
}
