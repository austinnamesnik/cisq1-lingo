package nl.hu.cisq1.lingo;

import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.presentation.dto.GameMapperIntegrationTest;
import nl.hu.cisq1.lingo.words.WordTestDataFixtures;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@Profile("ci")
@TestConfiguration
@ComponentScan(basePackageClasses = GameMapperIntegrationTest.class)
public class CiTestConfiguration {
    @Bean
    CommandLineRunner importWords(SpringWordRepository repository) {
        return new WordTestDataFixtures(repository);
    }
}
