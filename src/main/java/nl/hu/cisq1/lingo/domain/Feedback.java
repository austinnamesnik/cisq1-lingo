package nl.hu.cisq1.lingo.domain;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import nl.hu.cisq1.lingo.domain.exception.InvalidFeedbackException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Feedback implements Serializable {

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "attempt")
    private String attempt;

    @ElementCollection(targetClass = Mark.class)
    @CollectionTable(name = "marks", joinColumns = @JoinColumn(name = "feedback_id"))
    @Column(name = "marks")
    @Enumerated(EnumType.STRING)
    private List<Mark> marks;

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

    public Hint giveHint() {
        List<Character> hint = new ArrayList<>();
        int index = 0;
        for (Mark m : this.marks) {
            switch (m) {
                case ABSENT:
                    hint.add('-');
                    break;
                case PRESENT:
                    hint.add('+');
                    break;
                case CORRECT:
                    hint.add(this.attempt.charAt(index));
            }
            index++;
        }
        return new Hint(hint);
    }
}
