package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MainPage;
import pages.OurInitiativesPage;
import pages.TestData;
import pages.home.HomeMarketplacePage;
import pages.home.HomeUsersSignInPage;
import pages.top_menu.*;
import utils.ProjectConstants;

import java.util.List;

public class TopMenuTest extends BaseTest {

    @Test(priority = -5)
    public void testTopMenuLinkAmount() {
        final int expectedTopMenuLinkAmount = 14;

        int actualTopMenuLinkAmount =
                openBaseURL()
                        .topMenuLinkAmount();

        Assert.assertEquals(actualTopMenuLinkAmount, expectedTopMenuLinkAmount);
    }

    @Test
    public void testCompanyLogoNavigatesToBaseURL() {
        final String expectedURL = "https://openweathermap.org/";
        final String expectedTitle = "Сurrent weather and forecast - OpenWeatherMap";

        openBaseURL();

        String actualURL = new MainPage(getDriver())
                .clickLogo()
                .getCurrentURL();

        String actualTitle = new MainPage(getDriver()).getTitle();

        Assert.assertEquals(actualURL, expectedURL);
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Test
    public void testPlaceholderIsAvailable_TopMenu() {
        final String expectedInnerTextOfPlaceholder = "Weather in your city";
        final String attribute = "placeholder";

        MainPage mainPage = openBaseURL();
        Assert.assertTrue(mainPage.isPlaceholderDisplayed());

        String actualInnerTextOfPlaceholder = mainPage.getInnerTextOfPlaceholder(attribute);

        Assert.assertEquals(actualInnerTextOfPlaceholder, expectedInnerTextOfPlaceholder);
    }

    @Test
    public void testNavigationBarSearchField_NavigatesToFindPage() {
        final String expectedURL = "https://openweathermap.org/find?q=Barcelona";
        final String expectedName = "Barcelona";

        final String oldURL = openBaseURL().getCurrentURL();

        String actualURL = new MainPage(getDriver())
                .clickSearchFieldTopMenu()
                .inputSearchCriteriaAndEnter(expectedName)
                .getCurrentURL();

        Assert.assertNotEquals(oldURL, actualURL);
        Assert.assertEquals(actualURL, expectedURL);
    }

    @Test
    public void testSupportMenuLinksTexts() {
        final List<String> expectedList = List.of(
                "FAQ",
                "How to start",
                "Ask a question"
        );

        List actualList =
                openBaseURL()
                        .clickSupportMenu()
                        .getLinksText();

        Assert.assertEquals(actualList, expectedList);
    }

    @Test
    public void testSupportMenuIsClickable() {
        final String expectedIfVisible = "dropdown-menu dropdown-visible";
        final String expectedIfNotVisible = "dropdown-menu";

        openBaseURL();

        String actualIfVisible = new MainPage(getDriver())
                .clickSupportMenu()
                .getSupportMenuIsActiveValue();

        Assert.assertTrue(new MainPage(getDriver()).isSupportDropdownContainerDisplayed());
        Assert.assertEquals(actualIfVisible, expectedIfVisible);

        String actualIfNotVisible = new MainPage(getDriver())
                .clickSupportMenu()
                .getSupportMenuIsActiveValue();

        Assert.assertEquals(actualIfNotVisible, expectedIfNotVisible);
    }

    @Test(dataProvider = "TopMenuTestData", dataProviderClass = TestData.class)
    public void testEachTopMenuNavigatesToCorrespondingPage(
            int index, String menuName, String href, String expectedURL, String expectedTitle) {

        MainPage mainPage = openBaseURL();

        String oldURL = mainPage.getCurrentURL();
        String oldTitle = mainPage.getTitle();

        mainPage.clickTopMenu(index);

        String actualURL = getDriver().getCurrentUrl();
        String actualTitle = getDriver().getTitle();

        if (index != 0) {
            Assert.assertNotEquals(actualURL, oldURL);
            Assert.assertNotEquals(actualTitle, oldTitle);
        }

        if (index != 6) {
            Assert.assertEquals(actualURL, expectedURL);
        } else {
            Assert.assertTrue(actualURL.contains(expectedURL));
        }

        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Test(dataProvider = "ExternalTopMenuTestData", dataProviderClass = TestData.class)
    public void testExternalTopMenuNavigatesToCorrespondingPage(
            int index, String menuName, String href, String expectedURL, String expectedTitle) {

        MainPage mainPage = openBaseURL();

        String oldURL = mainPage.getCurrentURL();
        String oldTitle = mainPage.getTitle();

        mainPage.clickTopMenuExternalLink(index);

        String actualURL = getExternalPageURL();
        String actualTitle = getExternalPageTitle();

        if (index != 0) {
            Assert.assertNotEquals(actualURL, oldURL);
            Assert.assertNotEquals(actualTitle, oldTitle);
            Assert.assertEquals(actualURL, expectedURL);
            Assert.assertEquals(actualTitle, expectedTitle);
        }
    }

    @Test
    public void testHamburgerMenuIsAvailableAndHasOptions_TopMenu() {
        final int expectedNumberOfOptionsHamburgerMenu = 12;
        final List<String> expectedHamburgerMenuList = List.of(
                "Guide", "API", "Dashboard", "Marketplace", "Pricing",
                "Maps", "Our Initiatives", "Partners", "Blog", "For Business",
                "Ask a question", "Sign in"
        );
        openBaseURL()
                .setWindowWithHamburgerMenu(ProjectConstants.WIDTH_HAMBURGER_MENU, ProjectConstants.HEIGHT_HAMBURGER_MENU)
                .clickHamburgerMenuIcon();

        int actualNumberOfOptionsHamburgerMenu = new MainPage(getDriver()).getNumberOfOptionsHamburgerMenu();

        List<String> actualHamburgerMenuList = new MainPage(getDriver()).getHamburgerMenuList();

        Assert.assertTrue(new MainPage(getDriver()).isHamburgerIconDisplayed());
        Assert.assertTrue(new MainPage(getDriver()).getNumberOfOptionsHamburgerMenu() > 0);
        Assert.assertEquals(actualNumberOfOptionsHamburgerMenu, expectedNumberOfOptionsHamburgerMenu);
        Assert.assertEquals(actualHamburgerMenuList, expectedHamburgerMenuList);
    }

    @Test
    public void testHamburgerMenuHasLogo() {
        final String expectedURL = "https://openweathermap.org/";

        openBaseURL();

        String actualURL = new MainPage(getDriver())
                .setWindowWithHamburgerMenu(ProjectConstants.WIDTH_HAMBURGER_MENU, ProjectConstants.HEIGHT_HAMBURGER_MENU)
                .clickLogo().getCurrentURL();

        Assert.assertTrue(new MainPage(getDriver()).isLogoIconDisplayed());

        Assert.assertEquals(actualURL, expectedURL);
    }
}
