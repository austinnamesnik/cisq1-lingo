package nl.hu.cisq1.lingo.domain.exception;

public class RoundNotFinishedException extends RuntimeException {
    public RoundNotFinishedException() {
        super("Round is not finished. Please continue guessing.");
    }
}
