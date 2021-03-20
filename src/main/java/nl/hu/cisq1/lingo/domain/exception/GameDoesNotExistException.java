package nl.hu.cisq1.lingo.domain.exception;

public class GameDoesNotExistException extends RuntimeException {
    public GameDoesNotExistException() {
        super("This game does not exist");
    }
}
