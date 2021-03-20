package nl.hu.cisq1.lingo.domain;

import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.InvalidRoundException;

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
public class Game implements Serializable {
    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score")
    private int score = 0;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private List<Round> rounds = new ArrayList<>();

    public Game(Long id) {
        this.id = id;
    }

    public void startNextRound(Word word) {
        int roundNumber = this.rounds.size() + 1;
        Round round = new Round(roundNumber, word);
        round.startRound();
        this.rounds.add(round);
    }

    public Round getLastRound() {
        if (!this.rounds.isEmpty()) {
            return this.rounds.get(this.rounds.size()-1);
        }
        return null;
    }

    public void addScore(Round round, int attempts) {
        if (!this.rounds.contains(round)) {
            throw new InvalidRoundException();
        }

        if (attempts > 5) {
            throw new AttemptLimitReachedException();
        }

        this.score += 5 * (5 - attempts) + 5;
    }

    public void makeGuess(String attempt) {
        this.getLastRound().guessWord(new Word(attempt));
        if (this.getLastRound().wordIsGuessed()) {
            this.addScore(this.getLastRound(), this.getLastRound().getAttempts());
        }
    }
}
