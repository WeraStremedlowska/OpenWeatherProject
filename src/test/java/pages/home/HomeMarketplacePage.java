package pages.home;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.TestUtils;

import java.util.List;

public class HomeMarketplacePage extends HomeFooterMenuPage<HomeMarketplacePage> {

    final static String HOME_MARKETPLACE_BUTTONS_CONTAINER = "//div[@class='button-container']/a";

    @FindBy(xpath = HOME_MARKETPLACE_BUTTONS_CONTAINER)
    private List<WebElement> allHomeMarketplaceButtons;

    @FindBy(xpath = "//div[@class='category']//a[contains(@href, '/zip_code_data/new') and text()='Historical Weather Data by State for all ZIP codes, USA']")
    private WebElement weatherDataByStateMenu;

    public HomeMarketplacePage(WebDriver driver) {
        super(driver);
    }

    public HomeMarketplacePage createGeneric() {

        return new HomeMarketplacePage(getDriver());
    }

    public HomeMarketplacePage switchToMarketplaceWindow() {
        switchToAnotherWindow();
        TestUtils.waitForPageLoaded(getDriver());

        return this;
    }

    public List<WebElement> getAllHomeMarketplaceButtons() {

        return allHomeMarketplaceButtons;
    }

    public void waitUntilButtonIsClickable(WebElement button) {
        wait10ElementToBeClickable(button);
    }

    public HomeZipCodeDataNewPage clickWeatherDataByStateMenu() {
        click20(weatherDataByStateMenu);

        return new HomeZipCodeDataNewPage(getDriver());
    }
}
