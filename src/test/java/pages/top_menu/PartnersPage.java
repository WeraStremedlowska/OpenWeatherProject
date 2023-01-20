package pages.top_menu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.base_abstract.BreadCrumbPage;

import java.util.List;

public class PartnersPage extends BreadCrumbPage<PartnersPage> {

    @FindBy(xpath = "//div[@class = 'doc-container']//li")
    private List<WebElement> rightSideLinks;

    public PartnersPage(WebDriver driver) {
        super(driver);
    }

    public PartnersPage createGeneric() {

        return new PartnersPage(getDriver());
    }

    public List<String> getRightSideLinksText() {

        return getTexts(rightSideLinks);
    }
}

