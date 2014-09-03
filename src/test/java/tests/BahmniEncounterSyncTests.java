package tests;

import categories.ShrUiTest;
import data.*;
import domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

@Category(ShrUiTest.class)
public class BahmniEncounterSyncTests {

    private WebDriver driver;
    protected Patient primaryPatient;
    protected Diagnosis firstDiagnosis;
    protected Diagnosis secondDiagnosis;
    protected ChiefComplain firstChiefComplain;
    protected ChiefComplain secondChiefComplain;
    protected ChiefComplain thirdChiefComplain;
    protected Vitals patientVitals;
    protected FamilyHistory patientFamilyHistory;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @Test
    public void verifyDiagnosisSync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;
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
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;
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
                .validateChiefComplainData(firstChiefComplain).validateChiefComplainData(secondChiefComplain)
                .validateChiefComplainData(thirdChiefComplain);
    }

    @Test
    public void verifyVitalSync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;
        patientVitals = VitalsData.patientVitals;

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).goToHomePage()
                .goToClinicalPage().goToPatientDashboard(primaryPatient)
                .goToConsultationPage().goToObservationPage()
                .enterVitals(patientVitals);

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .validateVitals(patientVitals);
    }

    @Test
    public void verifyFamilyHistorySync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;
        patientFamilyHistory = FamilyHistoryData.familyHistory;

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).goToHomePage()
                .goToClinicalPage().goToPatientDashboard(primaryPatient)
                .goToConsultationPage().goToObservationPage()
                .enterFamilyHistory(patientFamilyHistory);

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient).goToConsultationPage()
                .validateFamilyHistoryData(patientFamilyHistory);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
