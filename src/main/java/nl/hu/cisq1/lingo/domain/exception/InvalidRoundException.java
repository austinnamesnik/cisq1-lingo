package nl.hu.cisq1.lingo.domain.exception;

public class InvalidRoundException extends RuntimeException {
    public InvalidRoundException() {
        super("This round does not correspond to your game");
    }
}
