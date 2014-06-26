package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class PatientSearchPage extends Page {

    @FindBy(id = "localName")
    private WebElement nidSearch;
    @FindBy(className = "reg-srch-btn")
    private WebElement searchButton;
    @FindBy(className = "View")
    private WebElement viewButton;

    public WebDriver driver;

    public PatientSearchPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("name"));
            }
        });
    }

    public PatientSearchResultPage searchPatientByNID(String nid) {
        nidSearch.sendKeys(nid);
        nidSearch.submit();
        return initialize(webDriver, PatientSearchResultPage.class);




    }

    public PatientDetailsPage goToCreatePatientPage() {
        WebElement createNewPatient = driver.findElement(By.xpath("//ul[@class='top-nav']/li[2]/a"));
        createNewPatient.click();
        return initialize(webDriver, PatientDetailsPage.class);
    }
}
