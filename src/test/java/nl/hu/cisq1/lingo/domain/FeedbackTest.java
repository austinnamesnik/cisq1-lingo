package nl.hu.cisq1.lingo.domain;

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

class FeedbackTest {

    @Test
    @DisplayName("The word is guessed when all letters are correct")
    void wordIsGuessed() {
        Feedback fb = new Feedback("woord", Arrays.asList(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(fb.wordIsGuessed());
    }

    @Test
    @DisplayName("The word is not guessed when not all letters are correct")
    void wordIsNotGuessed() {
        Feedback fb = new Feedback("woord", Arrays.asList(Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT));
        assertFalse(fb.wordIsGuessed());
    }

    @Test
    @DisplayName("The guess is invalid when the guessed word does not have the same amount of letters as the word to guess")
    void guessIsInvalid() {
        Feedback fb = new Feedback("woord", Arrays.asList(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertTrue(fb.guessIsInvalid());
    }

    @Test
    @DisplayName("The guess is not invalid when the guessed word has the same amount of letters as the word to guess")
    void guessIsNotInvalid() {
        Feedback fb = new Feedback("woord", Arrays.asList(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT));
        assertFalse(fb.guessIsInvalid());
    }

    @Test
    @DisplayName("Exception is thrown when the amount of marks is not equal to the length of the word")
    void invalidFeedback() {
        List<Mark> marks = Arrays.asList(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID);
        assertThrows(InvalidFeedbackException.class, () -> new Feedback("woord", marks));
    }

    @ParameterizedTest(name = "#{index} - Test with values: {0}, {1}")
    @DisplayName("The hint is based on the current guess")
    @MethodSource("provideHintExamples")
    void giveHint(String word, List<Mark> marks, List<Character> output) {
        Feedback fb = new Feedback(word, marks);
        assertEquals(fb.giveHint().getCharacters(), output);
    }


    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("woord", Arrays.asList(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), Arrays.asList('w', 'o', 'o', 'r', 'd')),
                Arguments.of("woord", Arrays.asList(Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT), Arrays.asList('+', 'o', '-', '+', '-'))
        );
    }
}