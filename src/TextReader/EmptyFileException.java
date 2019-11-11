package TextReader;

public class EmptyFileException extends Exception {

    public EmptyFileException() {
        super();
    }

    public EmptyFileException(String message) {
        super(message);
    }
}