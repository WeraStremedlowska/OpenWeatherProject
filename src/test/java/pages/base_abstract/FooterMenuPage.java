package pages.base_abstract;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.WeatherStationsPage;
import pages.footer_menu.AboutUsPage;
import pages.footer_menu.WidgetsPage;

import java.util.List;

public abstract class FooterMenuPage<Generic> extends TopMenuPage<Generic> {

    private static final String FOOTER_MENU_ID = "//div[@id='footer-website']";

    @FindBy(xpath = FOOTER_MENU_ID)
    private WebElement footerMenu;


    @FindBy(xpath = FOOTER_MENU_ID + "//a[@href='/about-us']")
    private WebElement aboutUsFooterMenu;

    @FindBy(xpath = FOOTER_MENU_ID + "//a[@href='/widgets-constructor']")
    private WebElement widgetsFooterMenu;

    @FindBy(xpath = FOOTER_MENU_ID + "//div[@class='social']/a")
    private List<WebElement> socialPanelIconsFooterMenu;

    @FindBy(xpath = FOOTER_MENU_ID + "//a[@href='/stations']")
    private WebElement connectYourWeatherStationFooterMenu;

    @FindBy(xpath = FOOTER_MENU_ID + "//span[contains(text(), '©')]")
    private WebElement copyright;

    @FindBy(xpath = FOOTER_MENU_ID + "//p[text()='Subscription']")
    private WebElement subscription;

    @FindBy(xpath = FOOTER_MENU_ID + "//p[text()='Subscription']/parent::div/div/ul/li")
    private List<WebElement> subscriptionList;

    @FindBy(xpath = FOOTER_MENU_ID + "//a")
    private List<WebElement> footerMenuLinks;

    @FindBy(xpath = FOOTER_MENU_ID + "//div[@class='my-5']//a")
    private List<WebElement> storePanelIconsFooterMenu;

    @FindBy(className = "social")
    private WebElement socialPanelFooterMenu;

    @FindBy(xpath = FOOTER_MENU_ID + "//ul/li/a")
    private List<WebElement> innerFooterMenuLink;

    public FooterMenuPage(WebDriver driver) {
        super(driver);
    }

    public abstract Generic createGeneric();

    public int getSocialPanelSize() {

        return getListSize(socialPanelIconsFooterMenu);
    }

    public String getCopyright() {

        return getText(copyright);
    }

    public WebElement getFooterMenu() {

        return footerMenu;
    }

    public int getFooterMenuLinksCount() {
        areAllElementsVisibleAndClickable(footerMenuLinks);

        return getListSize(footerMenuLinks);
    }

    public int getStoresIconsCount() {

        return getListSize(storePanelIconsFooterMenu);
    }

    public List<WebElement> getInnerFooterMenuLinks() {

        return innerFooterMenuLink;
    }

    public Generic clickFooterMenu(int index) {
        click(getInnerFooterMenuLinks().get(index));
        if (getDriver().getWindowHandles().size() > 1) {
            switchToAnotherWindow();
        }

        return createGeneric();
    }

    public void clickFooterMenuExternalLink(int index) {
        click(getInnerFooterMenuLinks().get(index));
        switchToAnotherWindow();
        getWait20().until(ExpectedConditions.numberOfWindowsToBe(2));
    }

    public AboutUsPage clickAboutUsFooterMenu() {
        click20(aboutUsFooterMenu);

        return new AboutUsPage(getDriver());
    }

    public WidgetsPage clickWidgetsPageFooterMenu() {
        click(widgetsFooterMenu);

        return new WidgetsPage(getDriver());
    }

    public WeatherStationsPage clickConnectYourWeatherStationFooterMenu() {
        click(connectYourWeatherStationFooterMenu);

        return new WeatherStationsPage(getDriver());
    }

    public boolean isStorePanelDisplayed() {

        return areElementsInListDisplayed(storePanelIconsFooterMenu);
    }

    public boolean isSocialPanelDisplayed() {

        return isElementDisplayed(socialPanelFooterMenu);
    }

}
