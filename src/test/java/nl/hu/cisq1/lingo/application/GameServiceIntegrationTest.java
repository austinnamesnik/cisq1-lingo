package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@Import(CiTestConfiguration.class)
class GameServiceIntegrationTest {

    @Autowired
    private GameService service;

    @ParameterizedTest
    @DisplayName("the hint is shown correctly after making a guess")
    @MethodSource("provideGames")
    void makeGuess(String word, String attempt, List<Character> hint) {
        Game game = new Game(1L);
        game.startNextRound(new Word(word));
        GameDTO dto = this.service.makeGuess(game, attempt);
        assertEquals(hint, dto.getHint());
    }

    static Stream<Arguments> provideGames() {
        return Stream.of(
                Arguments.of("woord", "waard", List.of('w', '-', '-', 'r', 'd')),
                Arguments.of("austin", "aussie", List.of('a', 'u', 's', '-', 'i', '-')),
                Arguments.of("swimcup", "swimcip", List.of('s', 'w', 'i', 'm', 'c', '-', 'p')),
                Arguments.of("hallo", "incorrect", List.of('*', '*', '*', '*', '*', '*', '*', '*', '*'))
        );
    }
}
