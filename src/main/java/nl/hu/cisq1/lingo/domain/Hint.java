package nl.hu.cisq1.lingo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Hint {

    private List<Character> hint;

    public Hint(List<Character> characterList) {
        this.hint = characterList;
    }
}
