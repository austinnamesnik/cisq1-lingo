package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    @ParameterizedTest(name = "#{index} - Word is {0} and the score is {2} with {1} attempts")
    @DisplayName("Score is added when the round is finished")
    @MethodSource("provideRoundExamples")
    void addScore(String word, int attempts, int score) {
        Game game = new Game();
        game.startNextRound(new Word(word));
        Round round = game.getLastRound();
        round.setAttempts(attempts);
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
        Game game = new Game();
        game.startNextRound(new Word("hallo"));
        Round round = game.getRounds().get(0);
        round.setAttempts(6);
        game.addScore();
    }

    @Test
    @DisplayName("The next round is start when a word is given")
    void startNextRound() {
        Game game = new Game();
        game.startNextRound(new Word("word"));
        assertEquals(1, game.getRounds().size());
    }

}