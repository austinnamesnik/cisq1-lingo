package nl.hu.cisq1.lingo.domain.exception;

public class AttemptLimitReachedException extends RuntimeException {
    public AttemptLimitReachedException() {
        super("The attempt limit was reached, you may not make another guess");
    }
}
