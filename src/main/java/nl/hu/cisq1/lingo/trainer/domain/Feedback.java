package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.List;

@EqualsAndHashCode
@ToString
public class Feedback {

    private String attempt;
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        if (marks.size() == attempt.length()) {
            this.attempt = attempt;
            this.marks = marks;
        } else {
            throw new InvalidFeedbackException();
        }
    }

    public boolean wordIsGuessed() {
        return this.marks.stream().allMatch(Mark.CORRECT::equals);
    }

    public boolean wordIsNotGuessed() {
        return !this.marks.stream().allMatch(Mark.CORRECT::equals);
    }

    public boolean wordIsInvalid() {
        return this.marks.stream().allMatch(Mark.INVALID::equals);
    }

    public boolean wordIsNotInvalid() {
        return !this.marks.stream().allMatch(Mark.INVALID::equals);
    }
}
