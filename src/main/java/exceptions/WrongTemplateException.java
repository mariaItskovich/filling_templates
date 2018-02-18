package exceptions;

/**
 * Класс исключения неправильного шаблона
 */
public class WrongTemplateException extends RuntimeException {
    public WrongTemplateException(String message) {
        super(message);
    }
}
