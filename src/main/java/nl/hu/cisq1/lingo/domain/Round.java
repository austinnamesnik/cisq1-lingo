package nl.hu.cisq1.lingo.domain;

import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.InvalidFeedbackException;
import nl.hu.cisq1.lingo.domain.exception.WordIsGuessedException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Round implements Serializable {

    @Id
    @Column(name = "round_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private int number;

    @OneToOne
    @JoinColumn(name = "word")
    private Word word;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private List<Feedback> feedbackList;

    private int attempts;

    public Round(int number, Word word) {
        this.number = number;
        this.word = word;
        this.feedbackList = new ArrayList<>();
        this.attempts = 0;
    }

    public boolean wordIsGuessed() {
        return this.feedbackList.get(feedbackList.size()-1).wordIsGuessed();
    }

    public boolean guessIsInvalid() {
        return this.feedbackList.get(feedbackList.size()-1).guessIsInvalid();
    }

    public Hint startRound() {
        List<Character> hint = new ArrayList<>();
        hint.add(this.word.getValue().charAt(0));
        for (int i = 1; i < this.word.getLength(); i++) {
            hint.add('_');
        }
        return new Hint(hint);
    }

    public void guessWord(Word guess) {
        if (attempts > 0) {
            if (attempts == 5) {
                throw new AttemptLimitReachedException();
            } else if (this.feedbackList.get(this.feedbackList.size()-1).wordIsGuessed()) {
                throw new WordIsGuessedException();
            }
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
        List<Integer> directMatch = new ArrayList<>(guess.getLength());
        List<Character> remainingCharacters = new ArrayList<>();
        for (int i = 0; i < guess.getLength(); i++) {
            Character guess_c = guess.getValue().charAt(i);
            Character word_c = this.word.getValue().charAt(i);
            if (guess_c.equals(word_c)) {
                directMatch.add(1);
            } else {
                directMatch.add(0);
            }
        }

        for (int i = 0; i < guess.getLength(); i++) {
            int x = directMatch.get(i);
            if (x == 0) {
                remainingCharacters.add(this.word.getValue().charAt(i));
            }
        }

        for (int i = 0; i < guess.getLength(); i++) {
            int x = directMatch.get(i);
            if (x == 0) {
                Character c = guess.getValue().charAt(i);
                if (remainingCharacters.contains(c)) {
                    marks.add(Mark.PRESENT);
                    remainingCharacters.remove(c);
                } else {
                    marks.add(Mark.ABSENT);
                }
            } else if (x == 1) {
                marks.add(Mark.CORRECT);
            }
        }
        return marks;
    }
}
