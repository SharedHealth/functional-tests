package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;


public class TRHomePage extends Page {
    @FindBy(linkText = "Dictionary")
    private WebElement dictionaryLink;

    @FindBy(linkText = "Administration")
    private WebElement administrationLink;

    public WebDriver driver;

    public TRHomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.linkText("Dictionary"));
            }
        });
      }

    public TRAdministrationPage goToAdministrationPage() {
        administrationLink.click();
        return initialize(driver, TRAdministrationPage.class);
    }
}
