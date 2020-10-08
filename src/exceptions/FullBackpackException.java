package exceptions;

public class FullBackpackException extends RuntimeException {
    public FullBackpackException() {
        super("Cannot add new item, the backpack is already full");
    }
}
