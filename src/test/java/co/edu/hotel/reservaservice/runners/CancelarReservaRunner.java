package co.edu.hotel.reservaservice.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "co.edu.hotel.reservaservice.steps",
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        plugin = {"pretty"},
        monochrome = true
)
public class CancelarReservaRunner {
}
