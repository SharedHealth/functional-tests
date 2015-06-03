package tests.datasetup;

import data.ConceptData;
import domain.Concept;
import domain.ConceptReferenceTerm;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.TRLoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class DataSetUpTests {

    private WebDriver driver = new FirefoxDriver();
    protected ConceptReferenceTerm conceptReferenceTerm;
    protected Concept concept;

    /**
     * These tests will remain ignored
     * These would be used as a one time run to setup data on any environment on need basis.
     */

    @Test
    @Ignore
    public void crateDataForEncounterSyncTest() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.referenceTermForEncounterSync;
        concept = dataStore.conceptWithReferenceTermForEncounterSync;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage()
                .goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm)
                .goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage()
                .searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }


    @Test
    @Ignore
    public void crateDataForEncounterSyncTest2() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        concept = dataStore.conceptWithOutReferenceTermForEncounterSync;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToConceptDictionaryMaintenancePage()
                .goToCreateNewConcept().createConceptWithOutReferenceTerm(concept);
    }

    @Test
    @Ignore
    public void crateDataForChiefComplainSyncTest() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.referenceTermForChiefComplainSync;
        concept = dataStore.conceptWithReferenceTermForChiefComplainSync;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage()
                .goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm)
                .goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage()
                .searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }


    @Test
    @Ignore
    public void crateDataForChiefComplainTest2() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        concept = dataStore.conceptWithOutReferenceTermForChiefComplainSync;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToConceptDictionaryMaintenancePage()
                .goToCreateNewConcept().createConceptWithOutReferenceTerm(concept);
    }

    @Test
    @Ignore
    public void createConceptForValueSet() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTermForValueSetData;
        concept = dataStore.conceptForValueSetData;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
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
