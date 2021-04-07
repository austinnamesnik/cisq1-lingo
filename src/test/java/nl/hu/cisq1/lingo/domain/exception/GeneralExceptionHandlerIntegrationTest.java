package nl.hu.cisq1.lingo.domain.exception;

import com.google.gson.Gson;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GeneralExceptionHandlerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    Game game;

    @BeforeEach
    void setup() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/create")
                .contentType(MediaType.APPLICATION_JSON);

        String json = mockMvc.perform(request1).andReturn().getResponse().getContentAsString();
        game = new Gson().fromJson(json, Game.class);
    }

    @Test
    @DisplayName("When searching for an invalid game, an exception should be thrown")
    void getInvalidGame() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .get("/get/0")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is(GameDoesNotExistException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("This game does not exist")));
    }

    @Test
    @DisplayName("When searching for a random word with an incorrect length, an exception should be thrown")
    void getInvalidWordByLength() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .get("/words/random")
                .param("length", "1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is(WordLengthNotSupportedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("Could not find word of length 1")));
    }

    @Test
    @DisplayName("When the attempt limit has been reached, and exception should be thrown")
    void makeAGuessTooMuch() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/next/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1);

        GuessDTO dto = new GuessDTO();
        dto.setAttempt("puzzi");
        String json = new Gson().toJson(dto);

        for (int i = 0; i < 6; i++) {
            RequestBuilder request2 = MockMvcRequestBuilders
                    .post("/guess/" + game.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json);

            mockMvc.perform(request2);
        }

        RequestBuilder request3 = MockMvcRequestBuilders
                .post("/guess/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(request3)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(AttemptLimitReachedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("The attempt limit was reached, you may not make another guess")));
    }

    @Test
    @DisplayName("When five incorrect attempts have been done during a round, an exception should be thrown")
    void gameIsFinishedAfterFiveIncorrectGuesses() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/next/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1);

        GuessDTO dto = new GuessDTO();
        dto.setAttempt("puzzi");
        String json = new Gson().toJson(dto);

        for (int i = 0; i < 6; i++) {
            RequestBuilder request2 = MockMvcRequestBuilders
                    .post("/guess/" + game.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json);

            mockMvc.perform(request2);
        }

        mockMvc.perform(request1)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(GameFinishedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("Game is finished! Start a new Game!")));
    }

    @Test
    @DisplayName("When a new round wants to start, but the previous round is not finished, an exception should be thrown")
    void startNewRoundTooSoon() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/next/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1);
        mockMvc.perform(request1)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(RoundNotFinishedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("Round is not finished. Please continue guessing.")));
    }

    @Test
    @DisplayName("When the word is guessed and another guess is made, an exception should be thrown")
    void makeGuessWhenWordIsGuessed() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/next/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1);

        GuessDTO dto = new GuessDTO();
        dto.setAttempt("pizza");
        String json = new Gson().toJson(dto);

        RequestBuilder request2 = MockMvcRequestBuilders
                .post("/guess/" + game.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request2);
        mockMvc.perform(request2)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(WordIsGuessedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("The word is guessed correctly, you can't make another guess")));
    }

}