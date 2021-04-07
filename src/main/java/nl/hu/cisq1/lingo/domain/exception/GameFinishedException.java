package nl.hu.cisq1.lingo.domain.exception;

public class GameFinishedException extends RuntimeException {
    public GameFinishedException() {
        super("Game is finished! Start a new Game!");
    }
}
