package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameDoesNotExistException;
import nl.hu.cisq1.lingo.presentation.dto.CreationDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
    private final SpringGameRepository gameRepository;

    private GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

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
        Game game = this.gameRepository.findById(id).orElseThrow(GameDoesNotExistException::new);
        return this.gameMapper.toCreationDTO(game);
    }

    public GameDTO startNextRound(Game game, String word) {
        game.startNextRound(new Word(word));
        return gameMapper.toGameDTOstart(game);
    }

    public GameDTO makeGuess(Game game, String attempt) {
        game.getLastRound().guessWord(new Word(attempt));
        return this.gameMapper.toGameDTOguess(game);
    }
}
