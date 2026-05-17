package vvss.pbinfo.pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

@DefaultUrl("https://www.pbinfo.ro")
public class HomePage extends PageObject {
    @FindBy(id = "user")
    private WebElementFacade userInputField;

    @FindBy(id = "parola")
    private WebElementFacade passInputField;

    @FindBy(xpath = "/html/body/div[1]/div[6]/div/div[3]/div[1]/div[1]/form/div/div[3]/div[2]/button")
    private WebElementFacade loginButton;

    @FindBy(xpath = "/html/body/div[1]/div[6]/div/div[3]/div[1]/div[1]/form/div/div[2]/div[3]/div")
    private WebElementFacade loginErrorMessage;

    @FindBy(xpath = "/html/body/div[1]/nav/div/div/ul[2]/li[5]/a")
    private WebElementFacade logoutButton;

    @FindBy(id = "search_box")
    private WebElementFacade searchInput;

    @FindBy(css = "button.fc-cta-do-not-consent")
    private WebElementFacade refuseCookiesButton;

    public void enterUsername(String username) {
        userInputField.waitUntilVisible().type(username);
    }

    public void enterPassword(String password) {
        passInputField.waitUntilVisible().type(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public boolean isErrorMessageDisplayed() {
        return loginErrorMessage.isVisible();
    }

    public boolean isLogoutButtonVisible() {
        return logoutButton.waitUntilVisible().isVisible();
    }

    public void clickLogout() {
        logoutButton.click();
    }

    public void searchForProblem(String name) {
        searchInput.typeAndEnter(name);
    }

    public void refuseCookies() {
        try {
            if (refuseCookiesButton.withTimeoutOf(Duration.ofSeconds(5)).isVisible()) {
                refuseCookiesButton.waitUntilClickable().click();
            }
        } catch (Exception e) {
            System.out.println("Nu a aparut cererea de cookies.");
        }
    }
}
