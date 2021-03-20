package nl.hu.cisq1.lingo.domain.exception;

public class WordIsGuessedException extends RuntimeException {
    public WordIsGuessedException() {
        super("The word is guessed correctly, you can't make another guess");
    }
}
