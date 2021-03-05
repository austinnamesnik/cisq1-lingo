package nl.hu.cisq1.lingo.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.InvalidFeedbackException;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Round {

    private final int number;
    private Word word;
    private List<Feedback> feedbackList;
    private int attempts;

    public Round(int number, Word word) {
        this.number = number;
        this.word = word;
        this.feedbackList = new ArrayList<>();
        this.attempts = 0;
    }

    public void startRound() {
        Feedback fb = new Feedback(this.word.getValue());
        this.feedbackList.add(fb);
    }

    public void guessWord(Word guess) {
        if (attempts == 5) {
            throw new AttemptLimitReachedException();
        }

        if (guess.getLength().equals(this.word.getLength())) {
            List<Mark> marks = this.generateMarks(guess);
            Feedback fb = new Feedback(guess.getValue(), marks);
            this.feedbackList.add(fb);
            this.attempts++;
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
