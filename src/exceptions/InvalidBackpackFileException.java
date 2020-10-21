package exceptions;

public class InvalidBackpackFileException extends RuntimeException {
    public InvalidBackpackFileException() {
        super("Backpack file has incorrect format. Please, make sure you use proper file");
    }
}
