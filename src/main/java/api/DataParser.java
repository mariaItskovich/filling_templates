package api;

/**
 * Интерфейс парсера данных, содержить метод parse и необходимые геттеры
 */
public interface DataParser {
    String getTemplatePath();
    String getInputPath();
    String getOutputPath();
    String getLeftPartPattern();
    String getRightPartPattern();
    void parse();
}
