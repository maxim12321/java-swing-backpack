package exceptions;

public class OpenFileException extends RuntimeException {
    public OpenFileException() {
        super("Something went wrong while trying to open a file");
    }
}
