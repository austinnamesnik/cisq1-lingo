package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Round {

    private int number;
    private Word word;
    private List<Feedback> feedbackList;

    public Round(int number, Word word) {
        this.number = number;
        this.word = word;
        this.feedbackList = new ArrayList<>();
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public List<Feedback> startRound() {
        Feedback fb = new Feedback(this.word.getValue());
        this.feedbackList.add(fb);
        return this.feedbackList;
    }

    public void guessWord(Word guess) {
        if (guess.getLength().equals(this.word.getLength())) {
            List<Mark> marks = this.generateMarks(guess);
            Feedback fb = new Feedback(guess.getValue(), marks);
            this.feedbackList.add(fb);
        } else {
            throw new InvalidFeedbackException();
        }
    }

    private List<Mark> generateMarks(Word guess) {
        List<Mark> marks = new ArrayList<>();
        for (int i = 0; i < guess.getLength(); i++) {
            Character c = guess.getValue().charAt(i);
            Character c2 = this.word.getValue().charAt(i);
            if (this.word.getValue().contains(Character.toString(c))) {
                if (c.equals(c2)) {
                    marks.add(Mark.CORRECT);
                } else {
                    marks.add(Mark.PRESENT);
                }
            } else {
                marks.add(Mark.ABSENT);
            }
        }
        return marks;
    }

}
