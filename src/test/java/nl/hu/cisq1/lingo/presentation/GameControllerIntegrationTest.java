package nl.hu.cisq1.lingo.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameControllerIntegrationTest {

    @Autowired
    private SpringGameRepository springGameRepository;

    @Autowired
    private SpringWordRepository springWordRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("creates a new game")
    void createsANewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/create")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.score", is(0)));
    }

    @Test
    @Order(2)
    @DisplayName("starts the next round of the game")
    void startsNextRound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/next/1")
                .contentType(MediaType.APPLICATION_JSON);

        String[] expectedResult = {"p", "_", "_", "_", "_"};

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(1)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.attempts", is(0)))
                .andExpect(jsonPath("$.feedbacks", hasSize(0)))
                .andExpect(jsonPath("$.hint", containsInRelativeOrder(expectedResult)));
    }

    @Test
    @Order(3)
    @DisplayName("makes a guess on the last round of the game")
    void guessWord() throws Exception {
        GuessDTO dto = new GuessDTO();
        dto.setAttempt("puzza");
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/guess/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        String[] expectedResult = {"p", "-", "z", "z", "a"};

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(1)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.attempts", is(1)))
                .andExpect(jsonPath("$.feedbacks", hasSize(1)))
                .andExpect(jsonPath("$.hint", containsInRelativeOrder(expectedResult)));
    }

    @Test
    @Order(4)
    @DisplayName("retrieves the game with the requested ID")
    void getsTheGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/get/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.score", is(0)));
    }

}
