package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.presentation.dto.CreationDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {

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

    @Test
    @DisplayName("throws exception when a game does not exist")
    void throwGameDoesNotExist() {
        SpringGameRepository mockRepo = mock(SpringGameRepository.class);
        when(mockRepo.findById(anyLong())).thenReturn(Optional.empty());

        GameService gs = new GameService(mockRepo);

        assertThrows(GameDoesNotExistException.class, () -> gs.findById(0L));
    }

    @ParameterizedTest
    @DisplayName("starts the next round of the game")
    @MethodSource("provideWords")
    void startNextRound(String word, String attempt, Game game) {
        SpringGameRepository sprGmeRepo = mock(SpringGameRepository.class);

        GameService gs = new GameService(sprGmeRepo);

        GameDTO dto = gs.startNextRound(game, word);

        assertEquals(dto.getGameId(), game.getId());
        assertEquals(game.getLastRound().getWord().getValue(), word);
        assertEquals(game.getRounds().size(), 1);
    }

    @ParameterizedTest
    @DisplayName("guesses a word in the last round of the game")
    @MethodSource("provideWords")
    void guessWord(String word, String attempt, Game game) {
        SpringGameRepository sprGmeRepo = mock(SpringGameRepository.class);

        GameService gs = new GameService(sprGmeRepo);

        gs.startNextRound(game, word);
        GameDTO dto = gs.makeGuess(game, attempt);

        assertEquals(dto.getAttempts(), 1);
        assertNotNull(dto.getFeedbacks());
        assertEquals(dto.getFeedbacks().size(), 1);
    }

    static Stream<Arguments> provideWords() {
        return Stream.of(
                Arguments.of("hallo", "hollo", new Game(1L)),
                Arguments.of("austin", "auston", new Game(2L)),
                Arguments.of("swimcup", "swimcup", new Game(3L))
        );
    }
}