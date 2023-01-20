package tests;

import base.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.home.HomePage;

import java.util.List;

public class HomeTest extends BaseTest {

    @Test
    public void testSuccessfulSignIn() {
        final String expectedSignInMessage = "Signed in successfully.";
        final String expectedUserMenuText = "Tester";

        String actualSignInMessage = openBaseURL()
                .signIn()
                .getNotification();

        String actualUserMenuText = new HomePage(getDriver()).getUserMenuText();

        Assert.assertEquals(actualSignInMessage, expectedSignInMessage);
        Assert.assertEquals(actualUserMenuText, expectedUserMenuText);
    }

    @Test
    public void testH2Headers() {
        final List<String> expectedH2Headers = List.of(
                "Historical weather for any location",
                "Weather Dashboard",
                "Agricultural Dashboard and Agro API"
        );

        openBaseURL().signIn();

        List<String> actualH2Headers = new HomePage(getDriver()).getListH2Headers();

        Assert.assertEquals(actualH2Headers, expectedH2Headers);
    }

    @Test
    public void testOrangeButtonsAreClickableAndVisible() {
        int count = 0;

        HomePage homePage = new HomePage(getDriver());

        List<WebElement> orangeButtons = openBaseURL()
                .signIn()
                .getOrangeButtons();

        for (WebElement button : orangeButtons) {
            if (button.isEnabled() && button.isDisplayed()) {
                homePage.waitUntilButtonIsClickable(button);
                count++;
            }
        }

        Assert.assertEquals(count, orangeButtons.size());
    }
}
