package nl.hu.cisq1.lingo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
public class Hint {

    private List<Character> characters;

    public Hint(List<Character> characterList) {
        this.characters = characterList;
    }
}
