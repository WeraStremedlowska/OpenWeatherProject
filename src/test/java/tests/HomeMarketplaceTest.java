package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.home.HomeZipCodeDataNewPage;


public class HomeMarketplaceTest extends BaseTest {

    @Test
    public void testWeatherDataByStateLink_NavigatesTo_HomeZipCodeDataPage() {
        final String expectedURL = "https://home.openweathermap.org/zip_code_data/new";
        final String expectedTitle = "Marketplace: History Bulk, History Forecast Bulk, Historical Weather Data by State for all ZIP codes, USA - OpenWeather";

        final String oldURL = openBaseURL()
                .clickMarketplaceMenu()
                .switchToMarketplaceWindow()
                .getCurrentURL();

        HomeZipCodeDataNewPage homeZipCodeDataNewPage = new HomeZipCodeDataNewPage(getDriver());

        homeZipCodeDataNewPage.clickWeatherDataByStateMenu();

        String actualURL = homeZipCodeDataNewPage.getCurrentURL();
        String actualTitle = homeZipCodeDataNewPage.getTitle();

        Assert.assertNotEquals(oldURL, actualURL);
        Assert.assertEquals(actualURL, expectedURL);
        Assert.assertEquals(actualTitle, expectedTitle);
    }
}
