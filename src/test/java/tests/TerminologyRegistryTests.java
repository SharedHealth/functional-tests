package tests;

import data.ConceptData;
import domain.Concept;
import domain.ConceptReferenceTerm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.TRLoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class TerminologyRegistryTests {

    static WebDriver driver;
    protected ConceptReferenceTerm conceptReferenceTerm;
    protected Concept concept;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @Test
    public void verifySyncOfFindingFromTR() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTermForFinding;
        concept = dataStore.conceptForFinding;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }

    @Test
    public void verifySyncOfEditFindingFromTR() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));

        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTermForFinding;
        concept = dataStore.conceptForFinding;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        concept = dataStore.conceptForFindingEdit;
        page.goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndEditConcept(concept).editConcept(concept);


        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }

    @Test
    public void verifyReferenceTermSyncFromTR() {

        driver.get(WebDriverProperties.getProperty("trExternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTerm;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToReferenceTermManagementPage()
                .searchAndEditReferenceWithWait(conceptReferenceTerm).verifyReferenceTerm(conceptReferenceTerm);
    }

    @Test
    public void verifyEditReferenceTermSyncFromTR() {

        driver.get(WebDriverProperties.getProperty("trExternalURL"));

        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTerm;
        concept = dataStore.conceptForDiagnosis;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm);

        conceptReferenceTerm = dataStore.conceptReferenceTermForEdit;
        page.goToTRAdministrationPage().goToReferenceTermManagementPage().searchAndEditReference(conceptReferenceTerm).editReferenceTerm(conceptReferenceTerm);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));
        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToReferenceTermManagementPage()
                .searchAndEditReferenceWithWait(conceptReferenceTerm).verifyReferenceTerm(conceptReferenceTerm);

    }

    @Test
    public void verifySyncOfNumericConceptsFromTR() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTermForNumericConcept;
        concept = dataStore.conceptForNumericConcept;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage()
                .goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }


    @After
    public void tearDown() {
        driver.quit();
    }


}
