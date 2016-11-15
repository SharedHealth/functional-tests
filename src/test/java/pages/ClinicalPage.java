package pages;

import domain.Patient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class ClinicalPage extends Page {

    @FindBy(id = "localName")
    private WebElement nidSearch;
    @FindBy(className = "reg-srch-btn")
    private WebElement searchButton;
    @FindBy(className = "View")
    private WebElement viewButton;

    public WebDriver driver;

    public ClinicalPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("patientIdentifier"));
            }
        });
    }


    public ClinicalDashboardPage goToPatientDashboard(Patient patient) {

        String xpath = "//div[text()='" + patient.getGiven() + " " + patient.getFamily() + "']";
        WebElement patientName = waitFindElementByXpath(xpath);
        patientName.click();
        return initialize(webDriver, ClinicalDashboardPage.class);
    }
}
