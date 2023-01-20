package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import pages.TestData;
import pages.home.HomeUsersSignInPage;

public class HomeUsersSignInTest extends BaseTest {

    @Test
    public void testSignOut() {
        String actualSignOutMessage = openBaseURL()
                .signIn()
                .signOut()
                .getNotification();

        Assert.assertEquals(actualSignOutMessage, "You need to sign in or sign up before continuing.");
    }

    @Test(dataProvider = "SignInCredentials", dataProviderClass = TestData.class)
    public void testSignInWithInvalidCredentials(
            String scenario, String userEmail, String userPassword, String expectedNoticeMessage, String expectedSignInMenuText) {

        final String oldSignInMenuText = openBaseURL()
                .clickSignInMenu()
                .getSignInText();

        HomeUsersSignInPage homeUsersSignInPage = new HomeUsersSignInPage(getDriver());

        homeUsersSignInPage
                .clickClearInputRegularUserEmail(userEmail)
                .clickClearInputRegularUserPassword(userPassword)
                .clickSubmitButton();

        String actualNoticeMessage = homeUsersSignInPage.getNotification();
        String actualSignInMenuText = homeUsersSignInPage.getSignInText();
        Reporter.log("In case of " + scenario + " User should see a notice " + homeUsersSignInPage.getNotification(), true);

        Assert.assertEquals(actualNoticeMessage, expectedNoticeMessage);
        Assert.assertEquals(actualSignInMenuText, oldSignInMenuText);
        Assert.assertEquals(actualSignInMenuText, expectedSignInMenuText);
    }
}
