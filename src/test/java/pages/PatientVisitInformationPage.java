package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class PatientVisitInformationPage extends Page {

    public WebDriver driver;


    @FindBy(id = "registrationFee")
    private WebElement consultationFee;

    @FindBy(id = "height")
    private WebElement height;

    @FindBy(id = "weight")
    private WebElement weight;

    @FindBy(id = "comments")
    private WebElement comments;

    @FindBy(xpath = "//button[@class='btn-user-info']")
    private WebElement userInfoButton;

    @FindBy(xpath = "//button[@class='confirm']")
    private WebElement saveButton;


    public PatientVisitInformationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.id("patientId")));
            }
        });

    }

    public void logout() {
        userInfoButton.click();
        WebElement logoutButton = driver.findElement(By.linkText("Logout"));
        logoutButton.click();
    }

    public PatientDetailsPage saveVisitInfoForNewPatient() {


        setText(height, "160");
        setText(weight, "60");
        setText(comments, "First Visit");
        setText(consultationFee, "10");
        saveButton.click();
        return initialize(webDriver, PatientDetailsPage.class);

    }

    public PatientSearchPage saveVisitInfo() {


        setText(height, "160");
        setText(weight, "60");
        setText(comments, "First Visit");
        setText(consultationFee, "10");
        saveButton.click();
        return initialize(webDriver, PatientSearchPage.class);

    }
}
