package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.MainPage;
import pages.TestData;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FooterMenuTest extends BaseTest {

    @Test
    public void testSocialPanelIconsNavigateToCorrespondingWebSites() {
        final List<String> links = List.of(
                "https://www.facebook.com/groups/270748973021342/?mibextid=6NoCDW",
                "https://twitter.com/OpenWeatherMap",
                "https://www.linkedin.com/uas/login?session_redirect=%2Fcompany%2F9816754",
                "https://openweathermap.medium.com/",
                "https://t.me/openweathermap",
                "https://github.com/search?q=openweathermap&ref=cmdform]"
        );

        final List<String> expectedDomains = List.of(
                "www.facebook.com",
                "twitter.com",
                "www.linkedin.com",
                "openweathermap.medium.com",
                "t.me",
                "github.com"
        );

        Assert.assertEquals(links.size(), expectedDomains.size());

        for (int i = 0; i < links.size(); i++) {
            String expectedDomain = expectedDomains.get(i);

            URL url = null;
            try {
                url = new URL(links.get(i));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Assert.assertNotNull(url);

            String actualDomain = url.getHost();

            Assert.assertEquals(actualDomain, expectedDomain);
        }
    }

    @Test
    public void testStorePanelExistsAndHasIcons() {
        final int expectedQuantity = 2;

        MainPage mainPage = openBaseURL()
                .scrollToFooterMenu();

        Assert.assertTrue(mainPage.isStorePanelDisplayed(), " StorePanel is not displayed ");
        Assert.assertEquals(mainPage.getStoresIconsCount(), expectedQuantity);
    }

    @Test
    public void testSocialPanelExistsAndHasIcons() {
        final int expectedIconsQuantity = 6;

        MainPage mainPage = openBaseURL()
                .scrollToFooterMenu();

        Assert.assertTrue(mainPage.isSocialPanelDisplayed(), " SocialPanel is not displayed ");
        Assert.assertEquals(mainPage.getSocialPanelSize(), expectedIconsQuantity);
    }

    @Test
    public void testCopyrightOnFooterMenu() {
        final String expectedCopyright = "© 2012 — 2023 OpenWeather ® All rights reserved";

        String actualCopyright = openBaseURL()
                .scrollToFooterMenu()
                .getCopyright();

        Assert.assertEquals(actualCopyright, expectedCopyright);
    }

    @Test(priority = -5)
    public void testFooterMenuLinksAmount() {
        final int expectedLinks = 27;

        int actualLinks = openBaseURL()
                .scrollToFooterMenu()
                .getFooterMenuLinksCount();

        Assert.assertEquals(actualLinks, expectedLinks);
    }

    @Test(dataProvider = "FooterMenuData", dataProviderClass = TestData.class)
    public void testFooterMenuLinksNavigateToCorrespondingPages(
            int index, String linkName, String href, String expectedURL, String expectedTitle) {

        MainPage mainPage = openBaseURL();

        final String oldURL = mainPage.getCurrentURL();
        final String oldTitle = mainPage.getTitle();

        String actualURL = mainPage.scrollToFooterMenu().clickFooterMenu(index).getCurrentURL();
        String actualTitle = getDriver().getTitle();

        Assert.assertNotEquals(oldURL, actualURL);
        Assert.assertNotEquals(oldTitle, actualTitle);
        Assert.assertEquals(actualURL, expectedURL);
        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Test(dataProvider = "ExternalFooterMenuData", dataProviderClass = TestData.class)
    public void testExternalMenuLinksNavigateToCorrespondingPages(
            int index, String linkName, String href, String expectedURL, String expectedTitle) {

        MainPage mainPage = openBaseURL();

        final String oldURL = mainPage.getCurrentURL();
        final String oldTitle = mainPage.getTitle();

        mainPage.scrollToFooterMenu().clickFooterMenuExternalLink(index);

            String actualURL = getExternalPageURL();
            String actualTitle = getExternalPageTitle();

            Assert.assertNotEquals(oldURL, actualURL);
            Assert.assertNotEquals(oldTitle, actualTitle);
            Assert.assertEquals(actualURL, expectedURL);
            Assert.assertEquals(actualTitle, expectedTitle);
        }
    }

