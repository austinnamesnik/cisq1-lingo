package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.WordIsGuessedException;
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

    Round r;
    Word w1;
    Word w2;

    @ParameterizedTest(name = "{0} - {1} was guessed and the hint was: {2}")
    @DisplayName("When a guess is made, the correct hint should be shown")
    @MethodSource("provideGuessExamples")
    void makeGuess(Word toGuess, Word guess, List<Character> chars) {
        r = new Round(1, toGuess);
        r.guessWord(guess);
        assertEquals(r.getLastFeedback().giveHint().getCharacters(), chars);
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
        r = new Round(1, new Word("tester"));
        r.setAttempts(5);
        r.guessWord(new Word("hihi"));
    }

    @Test
    @DisplayName("When the guessed word's length is not the same as the word to guess, an exception is thrown")
    void guessWithIncorrectLength() {
        w1 = new Word("woord");
        w2 = new Word("wod");
        r = new Round(1, w1);
        r.guessWord(w2);
        assertEquals(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID), r.getFeedbackList().get(0).getMarks());
    }

    @Test
    @DisplayName("When the game is finished, you can not make another guess")
    void makeGuessWhenFinished() {
        assertThrows(WordIsGuessedException.class, this::tester2);
    }

    void tester2() {
        w1 = new Word("hallo");
        w2 = new Word("hoiii");
        r = new Round(1, w1);
        r.guessWord(w1);
        r.guessWord(w2);
    }

    @Test
    @DisplayName("The word is guessed when the feedback contains all CORRECT")
    void wordIsGuessed() {
        w1 = new Word("junit");
        r = new Round(1, w1);
        r.guessWord(w1);
        assertTrue(r.wordIsGuessed());
    }

    @Test
    @DisplayName("The word is not guessed when there are no guesses made")
    void wordIsNotGuessedWithoutGuesses() {
        w1 = new Word("testing");
        r = new Round(1, w1);
        assertFalse(r.wordIsGuessed());
    }

    @Test
    @DisplayName("The word is not guessed when the feedback does not contain all CORRECT")
    void wordIsNotGuessedWithNonMatchingGuess() {
        w1 = new Word("games");
        w2 = new Word("gamer");
        r = new Round(1, w1);
        r.guessWord(w2);
        assertFalse(r.wordIsGuessed());
    }

    @Test
    @DisplayName("The guess is invalid when the feedback contains all INVALID")
    void guessIsInvalid() {
        w1 = new Word("carbon");
        w2 = new Word("start");
        r = new Round(1, w1);
        r.guessWord(w2);
        assertTrue(r.guessIsInvalid());
    }

    @Test
    @DisplayName("The guess is invalid when the feedback contains all INVALID")
    void guessIsNotInvalidWithoutGuesses() {
        w1 = new Word("carbon");
        r = new Round(1, w1);
        assertFalse(r.guessIsInvalid());
    }

    @Test
    @DisplayName("The guess is invalid when the feedback contains all INVALID")
    void guessIsNotInvalidWithNonInvalidGuess() {
        w1 = new Word("carbon");
        w2 = new Word("combos");
        r = new Round(1, w1);
        r.guessWord(w2);
        assertFalse(r.guessIsInvalid());
    }

    @Test
    @DisplayName("The last feedback is given when guesses have been made")
    void getLastFeedback() {
        w1 = new Word("force");
        w2 = new Word("fiery");
        r = new Round(1, w1);
        r.guessWord(w2);
        assertNotNull(r.getLastFeedback());
    }
}