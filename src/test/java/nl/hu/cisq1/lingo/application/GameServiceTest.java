package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Feedback;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {

    WordService wordService;
    @BeforeEach
    void start() {
        SpringWordRepository mockWordRepo = mock(SpringWordRepository.class);
        when(mockWordRepo.findRandomWordByLength(5))
                .thenReturn(Optional.of(new Word("pizza")));
        when(mockWordRepo.findRandomWordByLength(6))
                .thenReturn(Optional.of(new Word("oranje")));
        when(mockWordRepo.findRandomWordByLength(7))
                .thenReturn(Optional.of(new Word("wanorde")));

        wordService = new WordService(mockWordRepo);
    }

    @ParameterizedTest
    @DisplayName("finds a game by any ID and returns it")
    @MethodSource("provideGameAndID")
    void findByID(Game game, Long id) {
        SpringGameRepository mockGameRepo = mock(SpringGameRepository.class);
        when(mockGameRepo.findById(id))
                .thenReturn(Optional.of(new Game(id)));

        GameService gs = new GameService(mockGameRepo, wordService);
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

        GameService gs = new GameService(mockRepo, wordService);

        assertThrows(GameDoesNotExistException.class, () -> gs.findById(0L));
    }

    @ParameterizedTest
    @DisplayName("starts the next round of the game")
    @MethodSource("provideWords")
    void startNextRound(Game game) {
        SpringGameRepository sprGmeRepo = mock(SpringGameRepository.class);

        GameService gs = new GameService(sprGmeRepo, wordService);

        GameDTO dto = gs.startNextRound(game);

        assertEquals(dto.getGameId(), game.getId());
        assertEquals(1, game.getRounds().size());
    }

    @ParameterizedTest
    @DisplayName("guesses a word in the last round of the game")
    @MethodSource("provideWords")
    void guessWord(Game game, String attempt, int score) {
        SpringGameRepository sprGmeRepo = mock(SpringGameRepository.class);

        GameService gs = new GameService(sprGmeRepo, wordService);

        gs.startNextRound(game);
        GameDTO dto = gs.makeGuess(game, attempt);

        assertEquals(1, dto.getAttempts());
        assertNotNull(dto.getFeedback());
        assertEquals(score, game.getScore());
    }

    static Stream<Arguments> provideWords() {
        return Stream.of(
                Arguments.of(new Game(1L), "hollo", 0),
                Arguments.of(new Game(2L), "auston", 0),
                Arguments.of(new Game(3L), "swimcup", 0),
                Arguments.of(new Game(4L), "pizza", 25)
        );
    }
}