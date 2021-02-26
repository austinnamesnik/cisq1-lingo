package nl.hu.cisq1.lingo.trainer.domain;

import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("The word is guessed when all letters are correct")
    void wordIsGuessed() {
        Feedback fb = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(fb.wordIsGuessed());
    }

    @Test
    @DisplayName("The word is not guessed when not all letters are correct")
    void wordIsNotGuessed() {
        Feedback fb = new Feedback("woord", List.of(Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(fb.wordIsNotGuessed());
    }

    @Test
    @DisplayName("The guess is invalid when the guessed word does not have the same amount of letters as the word to guess")
    void guessIsInvalid() {
        Feedback fb = new Feedback("woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertTrue(fb.wordIsInvalid());
    }

    @Test
    @DisplayName("The guess is not invalid when the guessed word has the same amount of letters as the word to guess")
    void guessIsNotInvalid() {
        Feedback fb = new Feedback("woord", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT));
        assertTrue(fb.wordIsNotInvalid());
    }

}