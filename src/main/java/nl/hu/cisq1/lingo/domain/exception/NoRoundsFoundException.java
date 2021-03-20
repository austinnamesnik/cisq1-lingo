package nl.hu.cisq1.lingo.domain.exception;

public class NoRoundsFoundException extends RuntimeException {
    public NoRoundsFoundException() {
        super("No rounds during this game");
    }
}
