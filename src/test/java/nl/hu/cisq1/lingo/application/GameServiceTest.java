package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.presentation.dto.GameMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {
    static GameMapper mapper;

    @ParameterizedTest
    @DisplayName("finds a game by any ID and returns it")
    @MethodSource("provideGameAndID")
    void findByID(Game game, Long id) {
        SpringGameRepository mock = mock(SpringGameRepository.class);
        when(mock.findById(id))
                .thenReturn(Optional.of(new Game(id)));

        GameService gs = new GameService(mock);
        String result = gs.findById(id).toString();

        assertEquals(game.toString(), result);
    }

    static Stream<Arguments> provideGameAndID() {
        return Stream.of(
                Arguments.of(new Game(1L), 1L),
                Arguments.of(new Game(2L), 2L),
                Arguments.of(new Game(15L), 15L)
        );
    }
}