package nl.hu.cisq1.lingo.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.domain.exception.InvalidFeedbackException;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
public class Feedback {

    private final String attempt;
    private final List<Mark> marks;

    public Feedback(String attempt) {
        this(attempt, new ArrayList<>());
    }

    public Feedback(String attempt, List<Mark> marks) {
        if (marks.size() != attempt.length() && marks.size() != 0) {
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
        if (this.marks.isEmpty()) {
            hint.add(this.attempt.charAt(0));
            for (int i = 1; i < this.attempt.length(); i++) {
                hint.add('_');
            }
        } else {
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
        }
        return hint;
    }
}
