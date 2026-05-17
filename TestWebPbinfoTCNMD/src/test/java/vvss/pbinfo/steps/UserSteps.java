package vvss.pbinfo.steps;

import net.serenitybdd.annotations.Step;
import vvss.pbinfo.pages.HomePage;
import vvss.pbinfo.pages.ProblemPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserSteps {
    private HomePage homePage;
    private ProblemPage problemPage;

    @Step("Deschide pagina principala")
    public void openHomePage() {
        homePage.open();
        homePage.refuseCookies();
    }

    @Step("Login cu user {0} si parola {1}")
    public void login(String username, String password) {
        performLogin(username, password);
    }

    private void performLogin(String user, String pass) {
        homePage.enterUsername(user);
        homePage.enterPassword(pass);
        homePage.clickLogin();
    }

    @Step("Verificare login")
    public void shouldBeLoggedIn() {
        assertTrue(homePage.isLogoutButtonVisible());
    }

    @Step("Verifica eroarea de login")
    public void shouldSeeLoginError() {
        assertTrue(homePage.isErrorMessageDisplayed());
    }

    @Step("Cauta problema {0}")
    public void searchProblem(String name) {
        homePage.searchForProblem(name);
    }

    @Step("Verifica ca s-a ajuns pe pagina problemei")
    public void shouldBeOnUrl(String url) {
        assertEquals(problemPage.getDriver().getCurrentUrl(), url);
    }

    @Step("Adauga solutia")
    public void addSolution(String code) {
        problemPage.addSolution(code);
    }

    @Step("Verifica ca solutia e corecta")
    public void shouldSee100PointsMessage() {
        assertTrue(problemPage.isSolutionCorrect());
    }

    @Step("Logout")
    public void logout() {
        homePage.clickLogout();
    }
}
