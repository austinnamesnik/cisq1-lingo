package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(target = "game_id", source = "id")
    @Mapping(target = "number", expression = "java(game.getLastRound().getNumber())")
    @Mapping(target = "attempts", expression = "java(game.getLastRound().getAttempts())")
    @Mapping(target = "feedbacks", expression = "java(game.getLastRound().getFeedbackList())")
    @Mapping(target = "hint", expression = "java(game.getLastRound().startRound().getCharacters())")
    GameDTO toGameDTOstart(Game game);

    @Mapping(target = "game_id", source = "id")
    @Mapping(target = "number", expression = "java(game.getLastRound().getNumber())")
    @Mapping(target = "attempts", expression = "java(game.getLastRound().getAttempts())")
    @Mapping(target = "feedbacks", expression = "java(game.getLastRound().getFeedbackList())")
    @Mapping(target = "hint", expression = "java(game.getLastRound().getFeedbackList().get(game.getLastRound().getFeedbackList().size() - 1).giveHint().getCharacters())")
    GameDTO toGameDTOguess(Game game);

    CreationDTO toCreationDTO(Game game);
}
