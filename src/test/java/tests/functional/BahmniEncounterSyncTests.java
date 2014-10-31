package tests.functional;

import categories.ShrUiTest;
import data.*;
import domain.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.TestSetup;
import utils.WebDriverProperties;

@Category(ShrUiTest.class)
public class BahmniEncounterSyncTests extends TestSetup {

    protected Patient primaryPatient;
    protected Diagnosis firstDiagnosis;
    protected Diagnosis secondDiagnosis;
    protected ChiefComplain firstChiefComplain;
    protected ChiefComplain secondChiefComplain;
    protected ChiefComplain thirdChiefComplain;
    protected Vitals patientVitals;
    protected FamilyHistory patientFamilyHistory;
    private String facilityOneInternalURL = WebDriverProperties.getProperty("facilityOneInternalURL");
    private String facilityTwoInternalURL = WebDriverProperties.getProperty("facilityTwoInternalURL");
    @Test
    public void verifyDiagnosisSync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;
        firstDiagnosis = DiagnosisData.DiagnosisWithReferenceTermForEncounterSync;
        secondDiagnosis = DiagnosisData.DiagnosisWithOutReferenceTermForEncounterSync;


        driver.get(facilityOneInternalURL);

        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo").goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .goToConsultationPage().goToDiagnosisPage().enterDiagnosisDetails(firstDiagnosis, secondDiagnosis);


        driver.get(facilityTwoInternalURL);

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo").goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .ValidateEncounterData(firstDiagnosis).ValidateEncounterData(secondDiagnosis);
    }

    @Test
    public void verifyChiefComplainSync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;
        firstChiefComplain = ChiefComplainData.ChiefComplainWithReferenceTermForEncounterSync;
        secondChiefComplain = ChiefComplainData.ChiefComplainWithOutReferenceTermForEncounterSync;
        thirdChiefComplain = ChiefComplainData.NonCodedChiefComplainForEncounterSync;

        driver.get(facilityOneInternalURL);
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).goToHomePage()
                .goToClinicalPage().goToPatientDashboard(primaryPatient)
                .startConsultation().enterChiefComplainDetails(firstChiefComplain, secondChiefComplain, thirdChiefComplain);

        driver.get(facilityTwoInternalURL);

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.login("demo")
                .goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient).startConsultation().goToVisitPage()
                .validateChiefComplainData(firstChiefComplain).validateChiefComplainData(secondChiefComplain)
                .validateChiefComplainData(thirdChiefComplain);
    }

    @Test
    public void verifyVitalSync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;
        patientVitals = VitalsData.patientVitals;

        driver.get(facilityOneInternalURL);
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).goToHomePage()
                .goToClinicalPage().goToPatientDashboard(primaryPatient)
                .startConsultation().enterVitals(patientVitals);

        driver.get(facilityTwoInternalURL);
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .validateVitals(patientVitals);
    }

    @Test
    public void verifyFamilyHistorySync() {
        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;
        patientFamilyHistory = FamilyHistoryData.familyHistory;

        driver.get(facilityOneInternalURL);
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).goToHomePage()
                .goToClinicalPage().goToPatientDashboard(primaryPatient)
                .startConsultation().enterFamilyHistory(patientFamilyHistory);

        driver.get(facilityTwoInternalURL);
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo")
                .goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).startVisit(primaryPatient)
                .goToHomePage().goToClinicalPage().goToPatientDashboard(primaryPatient)
                .startConsultation().goToVisitPage()
                .validateFamilyHistoryData(patientFamilyHistory);
    }
}