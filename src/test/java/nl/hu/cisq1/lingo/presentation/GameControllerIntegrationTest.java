package nl.hu.cisq1.lingo.presentation;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("creates a new game")
    void createsANewGame() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/create")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", isA(Integer.class)))
                .andExpect(jsonPath("$.score", is(0))).andReturn().getResponse();
    }

    @Test
    @DisplayName("starts the next round of the game")
    void startsNextRound() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/create")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request1).andReturn().getResponse();

        Integer id = JsonPath.read(response.getContentAsString(), "$.id");

        RequestBuilder request2 = MockMvcRequestBuilders
                .post("/next/" + id)
                .contentType(MediaType.APPLICATION_JSON);

        String[] expectedResult = {"p", "_", "_", "_", "_"};

        mockMvc.perform(request2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(id)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.attempts", is(0)))
                .andExpect(jsonPath("$.feedback", nullValue()))
                .andExpect(jsonPath("$.hint", containsInRelativeOrder(expectedResult)));
    }

    @Test
    @DisplayName("makes a guess on the last round of the game")
    void guessWord() throws Exception {
        GuessDTO dto = new GuessDTO();
        dto.setAttempt("puzzi");
        String requestBody = new Gson().toJson(dto);

        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/create")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request1).andReturn().getResponse();

        Integer id = JsonPath.read(response.getContentAsString(), "$.id");

        RequestBuilder request2 = MockMvcRequestBuilders
                .post("/next/" + id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request2);

        RequestBuilder request3 = MockMvcRequestBuilders
                .post("/guess/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        String[] expectedResult = {"p", "-", "z", "z", "+"};

        mockMvc.perform(request3)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(id)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.attempts", is(1)))
                .andExpect(jsonPath("$.feedback.marks", hasSize(5)))
                .andExpect(jsonPath("$.hint", containsInRelativeOrder(expectedResult)));
    }

    @Test
    @DisplayName("retrieves the game with the requested ID")
    void getsTheGame() throws Exception {
        RequestBuilder request1 = MockMvcRequestBuilders
                .post("/create")
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request1).andReturn().getResponse();

        Integer id = JsonPath.read(response.getContentAsString(), "$.id");

        RequestBuilder request2 = MockMvcRequestBuilders
                .get("/get/" + id)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", isA(Integer.class)))
                .andExpect(jsonPath("$.score", is(0)));
    }

}
