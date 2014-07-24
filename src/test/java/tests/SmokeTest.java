package tests;

import data.ConceptData;
import data.PatientData;
import domain.Concept;
import domain.ConceptReferenceTerm;
import domain.Patient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import pages.TRLoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class SmokeTest {

    static WebDriver driver ;
    protected Patient primaryPatient;
    protected ConceptReferenceTerm conceptReferenceTerm;
    protected Concept concept;


    @Before
    public void setUp(){
        driver = new FirefoxDriver();

    }

    @Test
    public void verifyPatientSync(){

        driver.get(WebDriverProperties.getProperty("facilityOneExternalURL"));

        primaryPatient = PatientData.newPatient1;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();


        driver.get(WebDriverProperties.getProperty("facilityTwoExternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }

    @Test
    public void verifyConceptSyncFromTR(){

        driver.get(WebDriverProperties.getProperty("trExternalURL"));
        conceptReferenceTerm = ConceptData.conceptReferenceTerm;
        concept = ConceptData.conceptForDiagnosis;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin","Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin","test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConcept(concept).readCurrentConcept(concept);
    }


    @After
    public void tearDown(){
        driver.quit();
    }



}
