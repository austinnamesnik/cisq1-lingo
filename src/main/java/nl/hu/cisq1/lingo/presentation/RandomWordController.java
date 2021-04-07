package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.application.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("words")
public class RandomWordController {
    private final WordService service;

    public RandomWordController(WordService service) {
        this.service = service;
    }

    @GetMapping("random")
    public String getRandomWord(@RequestParam Integer length) {
        return this.service.provideRandomWord(length);
    }
}
