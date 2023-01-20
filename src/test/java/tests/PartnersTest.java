package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PartnersTest extends BaseTest {

    @Test
    public void testH1Header() {
        final String expectedHeader = "Partners and solutions";

        String actualHeader = openBaseURL()
                .clickPartnersMenu()
                .getH1Header();

        Assert.assertEquals(actualHeader, expectedHeader);
    }

    @Test
    public void testLinksTextsAreAsExpected() {
        final List<String> expectedRightSideLinks = List.of(
                "Google Weather-Based Campaign Management with OpenWeatherMap API"
                , "Google Maps JavaScript API based on OpenWeatherMap API"
                , "OpenWeather current weather data in Mozilla's IoT project"
                , "Ubuntu"
                , "Android"
                , "Leaflet"
                , "Java"
                , "Go (golang)"
                , "JavaScript"
                , "CMS"
                , "Raspberry Pi"
                , "Python"
                , "PHP"
                , "Apache Camel"
                , "Desktop"
                , "Mobile applications"
                , "Big library on GitHub"
        );

        List<String> actualRightSideLinks = openBaseURL()
                .clickPartnersMenu()
                .getRightSideLinksText();

        Assert.assertEquals(actualRightSideLinks, expectedRightSideLinks);
    }
}
