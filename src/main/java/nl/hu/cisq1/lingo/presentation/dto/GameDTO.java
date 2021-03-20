package nl.hu.cisq1.lingo.presentation.dto;

import lombok.AllArgsConstructor;
import nl.hu.cisq1.lingo.domain.Feedback;

import java.util.List;

@AllArgsConstructor
public class GameDTO {

    public Long game_id;
    public int number;
    public int attempts;
    public List<Feedback> feedbacks;
    public List<Character> hint;
}
