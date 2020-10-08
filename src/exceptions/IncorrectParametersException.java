package exceptions;

public class IncorrectParametersException extends RuntimeException {
    public IncorrectParametersException() {
        super("Failed to load shape: incorrect parameters amount");
    }
}
