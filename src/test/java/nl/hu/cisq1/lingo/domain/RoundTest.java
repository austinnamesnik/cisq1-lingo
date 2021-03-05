package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @ParameterizedTest(name = "{0} - The first letter is: {1}")
    @DisplayName("When the round starts, the first letter of the word should be shown")
    @MethodSource("provideNewWordsExamples")
    void startRound(Word word, Character c) {
        Round r = new Round(1, word);
        r.startRound();
        assertEquals(r.getFeedbackList().get(0).giveHint().get(0), c);
    }

    static Stream<Arguments> provideNewWordsExamples() {
        return Stream.of(
                Arguments.of(new Word("hallo"), 'h'),
                Arguments.of(new Word("tester"), 't'),
                Arguments.of(new Word("leet"), 'l')
        );
    }

    @ParameterizedTest(name = "{0} - {1} was guessed and the hint was: {2}")
    @DisplayName("When a guess is made, the correct hint should be shown")
    @MethodSource("provideGuessExamples")
    void makeGuess(Word toGuess, Word guess, List<Character> chars) {
        Round r = new Round(1, toGuess);
        r.guessWord(guess);
        assertEquals(r.getFeedbackList().get(0).giveHint(), chars);
    }

    static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of(new Word("woord"), new Word("waard"), Arrays.asList('w', '-', '-', 'r', 'd')),
                Arguments.of(new Word("woord"), new Word("wwwww"), Arrays.asList('w', '+', '+', '+', '+')),
                Arguments.of(new Word("heet"), new Word("hete"), Arrays.asList('h', 'e', '+', '+'))
        );
    }

    @Test
    @DisplayName("When 5 guesses have been made, guessing is not allowed")
    void makeGuessToMuch() {
        assertThrows(AttemptLimitReachedException.class, () -> {
            Round r = new Round(1, new Word("tester"));
            r.setAttempts(5);
            r.guessWord(new Word("hihi"));
        });
    }

    @Test
    @DisplayName("When the guessed word's length is not the same as the word to guess, an exception is thrown")
    void guessWithIncorrectLength() {
        assertThrows(InvalidFeedbackException.class, () -> {
            Round r = new Round(1, new Word("hidden"));
            r.guessWord(new Word("hid"));
        });
    }

}