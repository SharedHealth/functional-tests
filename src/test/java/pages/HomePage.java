package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;


public class HomePage extends Page {
    @FindBy(linkText = "Registration")
    private WebElement registrationButton;

    @FindBy(linkText = "Clinical")
    private WebElement clinicalButton;

    @FindBy(linkText = "National Registry")
    private WebElement nationalRegistrySearchButton;

    public WebDriver webDriver;

    public HomePage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.linkText("Registration"));
            }
        });
    }

    public PatientSearchPage goToRegistrationPage() {
        registrationButton.click();
        return initialize(webDriver, PatientSearchPage.class);
    }

    public ClinicalPage goToClinicalPage() {
        clinicalButton.click();
        return initialize(webDriver, ClinicalPage.class);
    }

    public MCIPatientSearchPage goToNationalRegistry() {

        System.out.println("Waiting 90 Secs for the sync to complete");
        waitForMillis(90000);
        nationalRegistrySearchButton.click();
        return initialize(webDriver, MCIPatientSearchPage.class);
    }

    public HomePage waitForCatchmentSync() {
        waitForMillis(125000);
        return initialize(webDriver, HomePage.class);
    }
}
