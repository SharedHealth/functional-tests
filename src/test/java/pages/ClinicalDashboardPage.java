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

    public ClinicalObservationsPage startConsultation() {
        consultationButton.click();
        return initialize(webDriver, ClinicalObservationsPage.class);
    }

    public ClinicalDashboardPage verifyEncounterData(Diagnosis expectedDiagnosis) {
        waitForMillis(1000);
        List<WebElement> diagnosisListElements = driver.findElements(By.xpath("//span[@class='diagnosis-name ng-binding']"));
        ArrayList<String> diagnosisList = new ArrayList<String>();

        for (WebElement diagnosis : diagnosisListElements)
            diagnosisList.add(diagnosis.getText());
        Assert.assertEquals("Diagnosis data not downloaded to Bahmni :" + expectedDiagnosis.getDiagnosisName(), true, diagnosisList.contains(expectedDiagnosis.getDiagnosisName()));
        System.out.println("Diagnosis Data verified for Patient :" + expectedDiagnosis.getDiagnosisName());
        return this;

    }


    public void verifyVitals(Vitals patientVitals) {
        waitForMillis(1000);

        List<WebElement> vitalsList = driver.findElements(By.xpath("//div[@class='tree-list-item ng-isolate-scope']"));
        HashMap<String, String> vitals = new HashMap();
        for (WebElement vital : vitalsList) {
            if (vital.findElements(By.cssSelector(".testUnderPanel")).size() > 0) {
                String vitalName = vital.findElement(By.cssSelector(".testUnderPanel")).getText();
                if (vital.findElements(By.cssSelector(".value-text-only")).size() > 0) {
                    String vitalValue = vital.findElement(By.cssSelector(".value-text-only")).getText();
                    vitals.put(vitalName, vitalValue);
                }
            }

        }
        Assert.assertEquals("Vital Verification Temperature ", patientVitals.getTemperature()+" F", vitals.get("Temperature"));
        Assert.assertEquals("Vital Verification Pulse ", patientVitals.getPulse()+" /min", vitals.get("Pulse"));
        Assert.assertEquals("Vital Verification Diastolic BP ", patientVitals.getDiastolicBloodPressure()+" mm Hg", vitals.get("Diastolic"));
        Assert.assertEquals("Vital Verification Systolic BP ", patientVitals.getSystolicBloodPressure()+" mm Hg", vitals.get("Systolic"));
        System.out.println("Vitals Data verified for the Patient ");
    }

    public DashBoardVisitPage goToVisitPage() {

        waitForMillis(1000);
        driver.findElement(By.xpath("(//visits-table//a)[1]")).click();
        return initialize(webDriver, DashBoardVisitPage.class);

    }
}
