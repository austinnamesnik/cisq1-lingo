package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.RoundNotFinishedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    Game game;
    Word w1;
    Word w2;
    Round r1;
    @BeforeEach
    void start() {
        game = new Game();
    }

    @ParameterizedTest(name = "#{index} - Word is {0} and the score is {2} with {1} attempts")
    @DisplayName("Score is added when the round is finished")
    @MethodSource("provideRoundExamples")
    void addScore(String word, int attempts, int score) {
        w1 = new Word(word);
        game.startNextRound(w1);
        r1 = game.getLastRound();
        r1.setAttempts(attempts);
        game.addScore();
        assertEquals(score, game.getScore());
    }

    private static Stream<Arguments> provideRoundExamples() {
        return Stream.of(
                Arguments.of("woord", 1, 25),
                Arguments.of("guess", 2, 20),
                Arguments.of("hallo", 3, 15),
                Arguments.of("doei", 4, 10),
                Arguments.of("maple", 5, 5)
        );
    }

    @Test
    @DisplayName("Exception is thrown when the round has too many attempt")
    void tooManyAttempts() {
        assertThrows(AttemptLimitReachedException.class, this::tester2);
    }

    void tester2() {
        w1 = new Word("hallo");
        game.startNextRound(w1);
        r1 = game.getLastRound();
        r1.setAttempts(6);
        game.addScore();
    }

    @Test
    @DisplayName("The next round is start when a word is given")
    void startNextRound() {
        w1 = new Word("word");
        game.startNextRound(w1);
        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("Throws exception when a new round wants to start when no guesses have been made")
    void startNextRoundInvalidNoGuesses() {
        w1 = new Word("word");
        game.startNextRound(w1);
        assertThrows(RoundNotFinishedException.class, () -> {
            w2 = new Word("hello");
            game.startNextRound(w2);
        });
    }

    @Test
    @DisplayName("Throws exception when a new round wants to start when the word is not guessed")
    void startNextRoundInvalidWordNotGuessed() {
        w1 = new Word("word");
        w2 = new Word("bird");
        game.startNextRound(w1);
        r1 = game.getLastRound();
        r1.guessWord(w2);
        assertThrows(RoundNotFinishedException.class, () -> {
            w1 = new Word("gates");
            game.startNextRound(w1);
        });
    }

}