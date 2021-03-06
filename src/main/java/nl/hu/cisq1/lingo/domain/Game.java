package nl.hu.cisq1.lingo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.GameFinishedException;
import nl.hu.cisq1.lingo.domain.exception.RoundNotFinishedException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
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
        if (this.getLastRound() != null && this.getLastRound().getAttempts() < 5) {
            if ((this.getLastRound().getLastFeedback() == null) || (!this.getLastRound().wordIsGuessed())) {
                throw new RoundNotFinishedException();
            }
        } else if (this.getLastRound() != null && this.getLastRound().getAttempts() == 5 && !this.getLastRound().wordIsGuessed()) {
            throw new GameFinishedException();
        }
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

    public void addScore() {
        if (this.getLastRound().getAttempts() > 5) {
            throw new AttemptLimitReachedException();
        }

        this.score += 5 * (5 - this.getLastRound().getAttempts()) + 5;
    }
}
