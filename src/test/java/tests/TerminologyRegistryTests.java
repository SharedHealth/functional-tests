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

    @After
    public void tearDown() {
        driver.quit();
    }


}
