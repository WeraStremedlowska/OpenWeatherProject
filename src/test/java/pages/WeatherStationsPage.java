package pages;

import org.openqa.selenium.WebDriver;
import pages.base_abstract.FooterMenuPage;

public class WeatherStationsPage extends FooterMenuPage<WeatherStationsPage> {

    public WeatherStationsPage(WebDriver driver) {
        super(driver);
    }

    public WeatherStationsPage createGeneric() {

        return new WeatherStationsPage(getDriver());
    }
}
