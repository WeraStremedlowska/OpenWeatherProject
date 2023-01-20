package pages.footer_menu;

import org.openqa.selenium.WebDriver;
import pages.base_abstract.FooterMenuPage;


public class AboutUsPage extends FooterMenuPage<AboutUsPage> {

    public AboutUsPage(WebDriver driver) {
        super(driver);
    }

    public AboutUsPage createGeneric() {

        return new AboutUsPage(getDriver());
    }
}
