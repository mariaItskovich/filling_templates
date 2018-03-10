package exceptions;

/**
 * Класс исключения операции создания файлов
 */
public class CreatingTemplateFilesException extends RuntimeException {
    public CreatingTemplateFilesException(String message) {
        super(message);
    }
}
