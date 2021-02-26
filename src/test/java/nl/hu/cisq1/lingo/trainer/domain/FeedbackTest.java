package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("The word is guessed when all letters are correct")
    void wordIsGuessed() {
        Mark[] marks = {Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT};
        Feedback fb = new Feedback("woord", Arrays.asList(marks));
        assertTrue(fb.wordIsGuessed());
    }

    @Test
    @DisplayName("The word is not guessed when not all letters are correct")
    void wordIsNotGuessed() {
        Mark[] marks = {Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT};
        Feedback fb = new Feedback("woord", Arrays.asList(marks));
        assertTrue(fb.wordIsNotGuessed());
    }

    @Test
    @DisplayName("The guess is invalid when the guessed word does not have the same amount of letters as the word to guess")
    void guessIsInvalid() {
        Mark[] marks = {Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID};
        Feedback fb = new Feedback("woord", Arrays.asList(marks));
        assertTrue(fb.wordIsInvalid());
    }

    @Test
    @DisplayName("The guess is not invalid when the guessed word has the same amount of letters as the word to guess")
    void guessIsNotInvalid() {
        Mark[] marks = {Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT};
        Feedback fb = new Feedback("woord", Arrays.asList(marks));
        assertTrue(fb.wordIsNotInvalid());
    }

}