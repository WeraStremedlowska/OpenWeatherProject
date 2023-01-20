package pages.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.base_abstract.FooterMenuPage;

public class HomeAskQuestionPage extends FooterMenuPage<HomeAskQuestionPage> {

    @FindBy(id = "question_form_email")
    private WebElement emailTextBox;

    @FindBy(id = "question_form_subject")
    private WebElement subjectTextBox;

    @FindBy(id = "question_form_message")
    private WebElement messageTextBox;

    @FindBy(xpath = "//div[@class='col-sm-8']//input[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@class='help-block']")
    private WebElement errorMessage;

    @FindBy(xpath = "//input[@id = 'question_form_is_user_true']")
    private WebElement yesRadioButton;

    @FindBy(xpath = "//input[@id = 'question_form_is_user_false']")
    private WebElement noRadioButton;

    @FindBy(xpath = "//input[@id='question_form_email']/following-sibling::span[@class = 'help-block']")
    private WebElement emailHelpBlock;

    @FindBy(xpath = "//div[@id='prompt']")
    private WebElement enterYourAccountMessage;

    public HomeAskQuestionPage(WebDriver driver) {
        super(driver);
    }

    public HomeAskQuestionPage createGeneric() {

        return new HomeAskQuestionPage(getDriver());
    }

    public String getErrorMessageText() {

        return getText(errorMessage);
    }

    public String getRadioButtonText() {

        return getText(enterYourAccountMessage);
    }

    public HomeAskQuestionPage clickOnSubmitButton() {
        click(submitButton);

        return this;
    }

    public HomeAskQuestionPage clickYesRadioButton() {
        click(yesRadioButton);

        return this;
    }

    public HomeAskQuestionPage inputTextInEmailTextbox(String text) {
        input(text, emailTextBox);

        return this;
    }

    public HomeAskQuestionPage inputTextInMessageTextbox(String text) {
        input(text, messageTextBox);

        return this;
    }

    public HomeAskQuestionPage selectSubject(String text) {
        selectOption(subjectTextBox, text);

        return this;
    }
}
