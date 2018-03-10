package exceptions;

/**
 * Класс исключения опреации создания файлов
 */
public class CreatingTemplateFilesException extends RuntimeException {
    public CreatingTemplateFilesException(String message) {
        super(message);
    }
}
