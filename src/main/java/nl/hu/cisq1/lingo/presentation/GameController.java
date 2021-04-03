package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.application.GameService;
import nl.hu.cisq1.lingo.application.WordService;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.presentation.dto.CreationDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
public class GameController {

    private GameService gameService;

    private WordService wordService;

    public GameController(GameService gameService, WordService wordService) {
        this.gameService = gameService;
        this.wordService = wordService;
    }

    @PostMapping("/create")
    public CreationDTO createGame() {
        return this.gameService.createGame();
    }

    @PostMapping("/next/{id}")
    public GameDTO startNextRound(@PathVariable("id") Long id) {
        Game game = this.gameService.findById(id);
        return this.gameService.startNextRound(game);
    }

    @PostMapping("/guess/{game_id}")
    public GameDTO guessWord(@PathVariable("game_id") Long gameId, @Validated @RequestBody GuessDTO guess) {
        Game game = this.gameService.findById(gameId);
        return this.gameService.makeGuess(game, guess.getAttempt());
    }

    @GetMapping("/get/{id}")
    public CreationDTO getGame(@PathVariable("id") Long id) {
        return this.gameService.findByIdCreation(id);
    }
}
