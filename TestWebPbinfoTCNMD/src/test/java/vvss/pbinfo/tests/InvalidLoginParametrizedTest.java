package vvss.pbinfo.tests;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;
import vvss.pbinfo.steps.UserSteps;

@ExtendWith(SerenityJUnit5Extension.class)
public class InvalidLoginParametrizedTest {
    @Managed(uniqueSession = true)
    private WebDriver webDriver;

    @Steps
    private UserSteps user;

    @ParameterizedTest
    @CsvFileSource(resources = "/users_invalid.csv", numLinesToSkip = 1)
    public void testLogin(String username, String password) {
        user.openHomePage();
        user.login(username, password);
        user.shouldSeeLoginError();
    }
}
