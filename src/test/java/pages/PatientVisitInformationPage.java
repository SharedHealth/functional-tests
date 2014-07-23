package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class PatientVisitInformationPage extends Page{

    public WebDriver driver;

    @FindBy(id = "patientId")
    private WebElement patientIdentified;

    @FindBy(xpath = "//button[@class='btn-user-info']")
    private WebElement userInfoButton;



    public PatientVisitInformationPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
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
    public void printPatientID() {
        System.out.println(patientIdentified.getAttribute("value"));
    }

    public void logout() {
        userInfoButton.click();
        WebElement logoutButton = driver.findElement(By.linkText("Logout"));
        logoutButton.click();
    }
}
