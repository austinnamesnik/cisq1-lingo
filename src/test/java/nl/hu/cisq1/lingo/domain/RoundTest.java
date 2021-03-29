package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.domain.exception.WordIsGuessedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoundTest {

    @ParameterizedTest(name = "{0} - {1} was guessed and the hint was: {2}")
    @DisplayName("When a guess is made, the correct hint should be shown")
    @MethodSource("provideGuessExamples")
    void makeGuess(Word toGuess, Word guess, List<Character> chars) {
        Round r = new Round(1, toGuess);
        r.guessWord(guess);
        assertEquals(r.getFeedbackList().get(0).giveHint().getCharacters(), chars);
    }

    static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of(new Word("woord"), new Word("waard"), Arrays.asList('w', '-', '-', 'r', 'd')),
                Arguments.of(new Word("woord"), new Word("wwwww"), Arrays.asList('w', '-', '-', '-', '-')),
                Arguments.of(new Word("heet"), new Word("hete"), Arrays.asList('h', 'e', '+', '+'))
        );
    }

    @Test
    @DisplayName("When 5 guesses have been made, guessing is not allowed")
    void makeGuessToMuch() {
        assertThrows(AttemptLimitReachedException.class, this::tester1);
    }

    void tester1() {
        Round r = new Round(1, new Word("tester"));
        r.setAttempts(5);
        r.guessWord(new Word("hihi"));
    }

    @Test
    @DisplayName("When the guessed word's length is not the same as the word to guess, an exception is thrown")
    void guessWithIncorrectLength() {
        Round r = new Round(1, new Word("woord"));
        r.guessWord(new Word("wod"));
        assertEquals(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID), r.getFeedbackList().get(0).getMarks());
    }

    @Test
    @DisplayName("When the game is finished, you can not make another guess")
    void makeGuessWhenFinished() {
        assertThrows(WordIsGuessedException.class, this::tester2);
    }

    void tester2() {
        Round round = new Round(1, new Word("hallo"));
        round.guessWord(new Word("hallo"));
        round.guessWord(new Word("hoiii"));
    }
}