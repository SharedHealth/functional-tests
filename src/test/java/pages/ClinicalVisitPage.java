package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class ClinicalVisitPage extends Page {

    public WebDriver driver;

    @FindBy(linkText = "Diagnosis")
    private WebElement diagnosisButton;


    public ClinicalVisitPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.linkText("Visit")));
            }
        });

    }


    public ClinicalDiagnosisPage goToDiagnosisPage() {
        diagnosisButton.click();
        return initialize(driver, ClinicalDiagnosisPage.class);
    }
}
