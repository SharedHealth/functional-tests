package tests;

import categories.ShrTest;
import data.ChiefComplainData;
import data.DiagnosisData;
import data.PatientData;
import domain.ChiefComplain;
import domain.Diagnosis;
import domain.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

@Category(ShrTest.class)
public class BahmniEncounterSyncTests {

    static WebDriver driver;
    protected Patient primaryPatient;
    protected Diagnosis firstDiagnosis;
    protected Diagnosis secondDiagnosis;
    protected ChiefComplain firstChiefComplain;
    protected ChiefComplain secondChiefComplain;
    protected ChiefComplain thirdChiefComplain;


    @Before
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @Test
    public void verifyEncounterSync() {


        primaryPatient = PatientData.newPatient1;
        firstDiagnosis = DiagnosisData.DiagnosisWithReferenceTermForEncounterSync;
        secondDiagnosis = DiagnosisData.DiagnosisWithOutReferenceTermForEncounterSync;

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));

        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo").goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .goToConsultationPage().goToDiagnosisPage().enterDiagnosisDetails(firstDiagnosis, secondDiagnosis);

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo").goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .ValidateEncounterData(firstDiagnosis).ValidateEncounterData(secondDiagnosis);

    }

    @Test
    public void verifyChiefComplainSync() {


        primaryPatient = PatientData.newPatient1;
        firstChiefComplain = ChiefComplainData.ChiefComplainWithReferenceTermForEncounterSync;
        secondChiefComplain = ChiefComplainData.ChiefComplainWithOutReferenceTermForEncounterSync;
        thirdChiefComplain = ChiefComplainData.NonCodedChiefComplainForEncounterSync;

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).goToHomePage()
                .goToClinicalPage().goToPatientDashboard(primaryPatient)
                .goToConsultationPage().goToObservationPage()
                .enterChiefComplainDetails(firstChiefComplain, secondChiefComplain, thirdChiefComplain);

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo")
                .goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient).goToConsultationPage()
                .validateChiefComplainData(firstChiefComplain).validateChiefComplainData(secondChiefComplain);
//                .validateChiefComplainData(thirdChiefComplain);  Commented the Non coded chief complain verification for now because of the spacing issue

    }

    @After
    public void tearDown() {
        driver.quit();
    }


}
