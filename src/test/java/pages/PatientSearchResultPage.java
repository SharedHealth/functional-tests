package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class PatientSearchResultPage extends Page {

    @FindBy(id = "localName")
    private WebElement nidSearch;
    @FindBy(className = "reg-srch-btn")
    private WebElement searchButton;
    @FindBy(className = "View")
    private WebElement viewButton;

    public WebDriver driver;

    public PatientSearchResultPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.xpath("//td[@class=\"edit-col\"]//button[1]")));
            }
        });
    }

    public PatientDetailsPage viewPatientDetails() {

        WebElement viewButton = driver.findElement(By.xpath("//td[@class=\"edit-col\"]//button"));
        viewButton.click();
        return initialize(webDriver, PatientDetailsPage.class);




    }
}
