package pages;

import domain.Diagnosis;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.List;

import static utils.PageFactoryWithWait.initialize;

public class ClinicalDashboardPage extends Page {

    public WebDriver driver;

    @FindBy(linkText = "Consultation")
    private WebElement consultationButton;


    public ClinicalDashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.linkText("Consultation")));
            }
        });

    }


    public ClinicalVisitPage goToConsultationPage() {
        consultationButton.click();
        return initialize(webDriver, ClinicalVisitPage.class);
    }

    public ClinicalDashboardPage ValidateEncounterData(Diagnosis expectedDiagnosis) {
        List<WebElement> diagnosisListElements = driver.findElements(By.xpath("//h4[@class='diagnosis-name ng-binding']"));
        ArrayList<String> diagnosisList = new ArrayList<String>();

        for (WebElement diagnosis : diagnosisListElements)
            diagnosisList.add(diagnosis.getText());
        Assert.assertEquals(true, diagnosisList.contains(expectedDiagnosis.getDiagnosisName()));
        System.out.println("Diagnosis Data verified for Patient :"+expectedDiagnosis.getDiagnosisName());
        return this;

    }
}
