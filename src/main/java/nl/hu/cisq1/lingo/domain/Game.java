package nl.hu.cisq1.lingo.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import nl.hu.cisq1.lingo.domain.exception.AttemptLimitReachedException;
import nl.hu.cisq1.lingo.domain.exception.InvalidRoundException;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;
    private int score;
    private List<Round> rounds;

    public Game(Long id) {
        this.id = id;
        this.score = 0;
        this.rounds = new ArrayList<>();
    }

    public void startNextRound(Word word) {
        int roundNumber = this.rounds.size() + 1;
        Round round = new Round(roundNumber, word);
        round.startRound();
        this.rounds.add(round);
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
}
