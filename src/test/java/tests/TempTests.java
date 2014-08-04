package tests;

import data.ConceptData;
import data.PatientData;
import domain.Concept;
import domain.ConceptReferenceTerm;
import domain.Patient;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import pages.TRLoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class TempTests {

    static WebDriver driver = new FirefoxDriver();
    protected Patient primaryPatient;
    protected ConceptReferenceTerm conceptReferenceTerm;
    protected Concept concept;


    @Test
    @Ignore
    public void verifyPatientSearchByNID() {

        driver.get(WebDriverProperties.getProperty("facilityTwoURL"));

        primaryPatient = PatientData.defaultPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().searchPatientByNID(primaryPatient.getNid()).viewPatientDetails().verifyPatientDetails(primaryPatient);


    }

    @Test
    @Ignore
    public void generateTimeStamp() {

        String id = String.valueOf(System.currentTimeMillis());
        System.out.println(id);
        id = id.substring(7);
        System.out.println(id);
    }

    @Test
    @Ignore
    public void verifyPatientSyncFromBahmni1() {

        driver.get(WebDriverProperties.getProperty("facilityOneExternalURL"));

        primaryPatient = PatientData.newPatient1;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo").goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();


        driver.get(WebDriverProperties.getProperty("facilityTwoExternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo").goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }

    @Test
    @Ignore
    public void verifyPatientSyncFromBahmni2() {

        driver.get(WebDriverProperties.getProperty("facilityTwoExternalURL"));

        primaryPatient = PatientData.newPatient2;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo").goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();


        driver.get(WebDriverProperties.getProperty("facilityOneExternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login("demo").goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }


    @Test
    @Ignore
    public void verifyConceptDetails() {

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTermForVerification;
        concept = dataStore.conceptForDiagnosisForVerification;


        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);

    }

    @Test
    public void createReferenceTerm() {

        driver.get(WebDriverProperties.getProperty("trExternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTerm;
        concept = dataStore.conceptForDiagnosis;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm);


    }



    @After
    public void tearDown() {
//        driver.quit();
    }


}
