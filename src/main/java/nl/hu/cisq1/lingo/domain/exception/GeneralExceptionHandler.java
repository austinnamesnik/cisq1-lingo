package nl.hu.cisq1.lingo.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({GameDoesNotExistException.class, WordLengthNotSupportedException.class})
    protected ResponseEntity<Object> notFoundExcpetion(Exception exception) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", exception.getClass().getSimpleName());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AttemptLimitReachedException.class, GameFinishedException.class, InvalidFeedbackException.class, RoundNotFinishedException.class, WordIsGuessedException.class})
    protected ResponseEntity<Object> conflictException(Exception exception) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", exception.getClass().getSimpleName());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
