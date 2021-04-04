package nl.hu.cisq1.lingo.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.hu.cisq1.lingo.domain.Feedback;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GameDTO {

    private Long gameId;
    private int number;
    private int attempts;
    private boolean wordGuessed;
    private List<Feedback> feedbacks;
    private List<Character> hint;
}
