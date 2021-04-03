package nl.hu.cisq1.lingo.domain;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@ToString
@NoArgsConstructor
@Entity(name = "words")
public class Word implements Serializable {
    @Id
    @Column(name = "word")
    private String value;
    private Integer length;

    public Word(String word) {
        this.value = word;
        this.length = word.length();
    }

    public String getValue() {
        return value;
    }

    public Integer getLength() {
        return length;
    }
}
