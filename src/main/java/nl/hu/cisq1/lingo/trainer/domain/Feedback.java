package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Feedback {

    private final String attempt;
    private final List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        if (marks.size() != attempt.length()) {
            throw new InvalidFeedbackException();
        }

        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean wordIsGuessed() {
        return this.marks.stream().allMatch(Mark.CORRECT::equals);
    }

    public boolean guessIsInvalid() {
        return this.marks.stream().allMatch(Mark.INVALID::equals);
    }

    public List<Character> giveHint() {
        List<Character> hint = new ArrayList<>();
        int index = 0;
        for (Mark m : this.marks) {
            switch (m) {
                case INVALID:
                    hint.add('*');
                    break;
                case ABSENT:
                    hint.add('-');
                    break;
                case PRESENT:
                    hint.add('+');
                    break;
                case CORRECT:
                    hint.add(this.attempt.charAt(index));
                    break;
            }
            index++;
        }
        return hint;
    }
}
