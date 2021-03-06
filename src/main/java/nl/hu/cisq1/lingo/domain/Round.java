package nl.hu.cisq1.lingo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.WordIsGuessedException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
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
        if (!this.feedbackList.isEmpty()) {
            return this.feedbackList.get(feedbackList.size()-1).wordIsGuessed();
        }
        return false;
    }

    public boolean guessIsInvalid() {
        if (!this.feedbackList.isEmpty()) {
            return this.feedbackList.get(feedbackList.size()-1).guessIsInvalid();
        }
        return false;
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

        List<Mark> marks;
        if (guess.getLength().equals(this.word.getLength())) {
            marks = this.generateMarks(guess);
            Feedback fb = new Feedback(guess.getValue(), marks);
            this.feedbackList.add(fb);
        } else {
            marks = this.generateInvalidMarks(guess);
            Feedback fb = new Feedback(guess.getValue(), marks);
            this.feedbackList.add(fb);
        }
        this.attempts++;
    }

    public Feedback getLastFeedback() {
        if (!this.feedbackList.isEmpty()) {
            return this.feedbackList.get(this.feedbackList.size()-1);
        }
        return null;
    }

    private List<Mark> generateMarks(Word guess) {
        List<Mark> marks = new ArrayList<>();
        List<Integer> directMatch = new ArrayList<>(guess.getLength());
        List<Character> remainingCharacters = new ArrayList<>();
        for (int i = 0; i < guess.getLength(); i++) {
            Character guessChar = guess.getValue().charAt(i);
            Character wordChar = this.word.getValue().charAt(i);
            if (guessChar.equals(wordChar)) {
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
            } else {
                marks.add(Mark.CORRECT);
            }
        }
        return marks;
    }

    private List<Mark> generateInvalidMarks(Word guess) {
        List<Mark> marks = new ArrayList<>();
        for (int i = 0; i < guess.getLength(); i++) {
            marks.add(Mark.INVALID);
        }
        return marks;
    }
}
