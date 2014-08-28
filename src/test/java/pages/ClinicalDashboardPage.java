package pages;

import domain.Diagnosis;
import domain.Vitals;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.HashMap;
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
        System.out.println("Diagnosis Data verified for Patient :" + expectedDiagnosis.getDiagnosisName());
        return this;

    }


    public void validateVitals(Vitals patientVitals) {
        waitForMillis(1000);

        List<WebElement> vitalsList = driver.findElements(By.cssSelector(".form-field"));
        HashMap<String, String> vitals = new HashMap();
        for (WebElement vital : vitalsList) {
            String vitalName = vital.findElement(By.cssSelector(".field-attribute")).getText();
            String vitalValue = vital.findElement(By.cssSelector(".value-text-only")).getText();
            vitals.put(vitalName, vitalValue);

        }

        Assert.assertEquals("Vital Verification Temperature ", vitals.get("Temperature-BD"), patientVitals.getTemperature());
        Assert.assertEquals("Vital Verification Pulse ", vitals.get("Pulse-BD"), patientVitals.getPulse());
        Assert.assertEquals("Vital Verification Diastolic BP ", vitals.get("Diastolic BD"), patientVitals.getDiastolicBloodPressure());
        Assert.assertEquals("Vital Verification Systolic BP ", vitals.get("Systolic BD"), patientVitals.getSystolicBloodPressure());
        System.out.println("Vitals Data verified for the Patient ");
    }
}
