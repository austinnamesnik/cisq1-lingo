package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = GameMapperIntegrationTest.SpringTestConfig.class)
class GameMapperIntegrationTest {

    @Configuration
    @ComponentScan(basePackageClasses = GameMapperIntegrationTest.class)
    static class SpringTestConfig {}

    @Autowired
    GameMapper mapper = Mappers.getMapper(GameMapper.class);

    Game g1;
    Game g2;
    Word w1;
    Word w2;
    @BeforeEach
    void start() {
        g1 = new Game(1L);
        g2 = null;
        w1 = new Word("start");
        w2 = new Word("slack");
    }

    @Test
    @DisplayName("Should inject GameMapper with function 'toGameDTOstart'")
    void toGameDTOstart() {
        g1.startNextRound(w1);
        GameDTO dto = mapper.toGameDTOstart(g1);
        assertNotNull(dto);
        assertEquals(1L, dto.getGameId());
        assertEquals(0, dto.getAttempts());
        assertEquals(1, dto.getNumber());
        assertEquals(List.of('s', '_', '_', '_', '_'), dto.getHint());
        assertEquals(0, dto.getFeedbacks().size());
    }

    @Test
    @DisplayName("Should return null if Game-object is null too")
    void toGameDTOstartNull() {
        GameDTO dto = mapper.toGameDTOstart(g2);
        assertNull(dto);
    }

    @Test
    @DisplayName("Should inject GameMapper with function 'toGameDTOguess'")
    void toGameDTOguess() {
        g1.startNextRound(w1);
        g1.getLastRound().guessWord(w2);
        GameDTO dto = mapper.toGameDTOguess(g1);
        assertNotNull(dto);
        assertEquals(1L, dto.getGameId());
        assertEquals(1, dto.getAttempts());
        assertEquals(1, dto.getNumber());
        assertEquals(List.of('s', '-', 'a', '-', '-'), dto.getHint());
        assertEquals(1, dto.getFeedbacks().size());
    }

    @Test
    @DisplayName("Should return null if Game-object is null too")
    void toGameDTOguessNull() {
        GameDTO dto = mapper.toGameDTOguess(g2);
        assertNull(dto);
    }

    @Test
    @DisplayName("Should inject GameMapper with function 'toCreationDTO'")
    void toCreationDTO() {
        CreationDTO dto = mapper.toCreationDTO(g1);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(0, dto.getScore());
    }

    @Test
    @DisplayName("Should return null if Game-object is null too")
    void toCreationDTOnull() {
        CreationDTO dto = mapper.toCreationDTO(g2);
        assertNull(dto);
    }

}
