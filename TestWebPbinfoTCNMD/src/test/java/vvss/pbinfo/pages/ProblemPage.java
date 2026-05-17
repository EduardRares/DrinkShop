package vvss.pbinfo.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class ProblemPage extends PageObject {
    @FindBy(id = "btn-submit")
    private WebElementFacade submitButton;

    @FindBy(css = "#detalii-evaluare h2 strong")
    private WebElementFacade scoreText;

    public void addSolution(String code) {
        // Deoarece PBInfo foloseste CodeMirror pentru textbox-ul cu cod, trebuie
        // sa adaugam codul din JavaScript
        String SET_CODE_SCRIPT = "document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);";
        evaluateJavascript(SET_CODE_SCRIPT, code);

        submitButton.waitUntilPresent();

        // Dam "click" prin JavaScript deoarece site-ul e plin de reclame si e posibil sa apara una
        // peste buton si sa dea peste cap Serenity
        evaluateJavascript("arguments[0].click()", submitButton);
    }

    public boolean isSolutionCorrect() {
        scoreText.withTimeoutOf(Duration.ofSeconds(30)).waitUntilVisible();
        return scoreText.getText().contains("100 puncte");
    }
}
