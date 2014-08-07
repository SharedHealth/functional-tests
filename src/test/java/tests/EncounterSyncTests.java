package tests;

import data.DiagnosisData;
import data.PatientData;
import domain.Diagnosis;
import domain.Patient;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class EncounterSyncTests {

    static WebDriver driver = new FirefoxDriver();
    protected Patient primaryPatient;
    protected Diagnosis firstDiagnosis;
    protected Diagnosis secondDiagnosis;


    @Test
    public void verifyEncounterSync() {


        primaryPatient = PatientData.newPatient1;
        firstDiagnosis = DiagnosisData.DiagnosisWithReferenceTermForEncounterSync;
        secondDiagnosis = DiagnosisData.DiagnosisWithOutReferenceTermForEncounterSync;

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));

        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo").goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).saveVisitInfoForNewPatient()
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .goToConsultationPage().goToDiagnosisPage().enterDiagnosisDetails(firstDiagnosis, secondDiagnosis);

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo").goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).startVisit(primaryPatient)
                .saveVisitInfo().goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .ValidateEncounterData(firstDiagnosis).ValidateEncounterData(secondDiagnosis);

    }

    @After
    public void tearDown() {
        driver.quit();
    }


}
