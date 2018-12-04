package exceptions;

/**
 * Класс исключения создания конечных файлов
 */
public class CreatingFilesException extends RuntimeException {
    public CreatingFilesException(String message) {
        super(message);
    }
}
