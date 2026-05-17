package vvss.pbinfo.tests;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import vvss.pbinfo.steps.UserSteps;

@ExtendWith(SerenityJUnit5Extension.class)
public class SolveProblemScenarioTest {
    @Managed(uniqueSession = true)
    private WebDriver webDriver;

    @Steps
    private UserSteps user;

    @Test
    public void testSolve_sum00() {
        user.openHomePage();
        user.login("vvss_dp19", "ode54321");
        user.shouldBeLoggedIn();
        user.searchProblem("nota");
        user.shouldBeOnUrl("https://www.pbinfo.ro/probleme/832/nota");

        String code = "#include <iostream>\n" +
                "\n" +
                "int main(){\n" +
                "    short n;\n" +
                "    std::cin >> n;\n" +
                "    \n" +
                "    if(n<5) std::cout << \"corigent\";\n" +
                "    else std::cout << \"promovat\";\n" +
                "    \n" +
                "    return 0;\n" +
                "    \n" +
                "}";

        user.addSolution(code);
        user.shouldSee100PointsMessage();

        user.logout();
    }
}
