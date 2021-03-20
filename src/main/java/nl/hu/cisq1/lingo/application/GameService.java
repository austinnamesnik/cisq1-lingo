package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Round;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.presentation.dto.CreationDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
    private final SpringGameRepository gameRepository;

    @Autowired
    private WordService wordService;

    @Autowired
    private GameMapper gameMapper;

    public GameService(SpringGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public CreationDTO createGame() {
        Game game = new Game();
        this.gameRepository.save(game);
        return gameMapper.toCreationDTO(game);
    }

    public Game findById(Long id) {
        return this.gameRepository.findById(id).orElseThrow(GameDoesNotExistException::new);
    }

    public CreationDTO findByIdCreation(Long id) {
        return this.gameMapper.toCreationDTO(this.gameRepository.findById(id).orElseThrow(GameDoesNotExistException::new));
    }

    public GameDTO startNextRound(Game game) {
        int letterLength = game.getRounds().size() % 3 + 5;
        game.startNextRound(new Word(wordService.provideRandomWord(letterLength)));
        return gameMapper.toGameDTOstart(game);
    }

    public GameDTO makeGuess(Game game, String attempt) {
        game.makeGuess(attempt);
        this.gameRepository.save(game);
        return this.gameMapper.toGameDTOguess(game);
    }
}
