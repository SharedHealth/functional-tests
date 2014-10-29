package tests.ApiTests;

import categories.TrUiTest;
import com.jayway.restassured.RestAssured;
import data.ConceptData;
import domain.Concept;
import domain.ConceptReferenceTerm;
import domain.Feed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.TRLoginPage;
import utils.FeedParser;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

import java.io.IOException;
import java.net.URL;

import static com.jayway.restassured.RestAssured.basic;
import static com.jayway.restassured.RestAssured.get;
import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

@Category(TrUiTest.class)
public class TerminologyRegistryTests {

    static WebDriver driver;
    protected ConceptReferenceTerm conceptReferenceTerm;
    protected Concept concept;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();

        RestAssured.baseURI = "http://172.18.46.56";
        RestAssured.port = 9080;
        RestAssured.authentication = basic("admin", "Admin123");
    }

    @Test
    public void verifyConceptSyncFromTR() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTerm;
        concept = dataStore.conceptForDiagnosis;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }

    @Test
    public void verifyThatConceptIsPublishedToFeed() throws IOException {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTerm;
        concept = dataStore.conceptForDiagnosis;

        Feed previous = new FeedParser().parse(new URL(WebDriverProperties.getProperty("trInternalURL") + "/ws/atomfeed/" + concept.getConceptClass() + "/recent").openConnection().getInputStream());

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123")
                .goToAdministrationPage()
                .goToReferenceTermManagementPage()
                .goToCreateReferenceTerm()
                .createReferenceTerm(conceptReferenceTerm)
                .goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage()
                .goToCreateNewConcept()
                .createConcept(concept);

        Feed current = new FeedParser().parse(new URL(WebDriverProperties.getProperty("trInternalURL") + "/ws/atomfeed/" + concept.getConceptClass() + "/recent").openConnection().getInputStream());

        assertTrue(current.hasMoreEntriesThan(previous));
    }

    @Test
    public void verifyGetConceptByUuidEndpoint() throws IOException {
        String trInternalUrl = WebDriverProperties.getProperty("trInternalURL");
        driver.get(trInternalUrl);
        Concept concept = new Concept.ConceptBuilder().name("true").build();
        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);

        String uuid = page.login("admin", "Admin123")
                .goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage()
                .searchAndViewConceptWithWait(concept)
                .readCurrentConceptAttr("UUID");

        String url = trInternalUrl + "/ws/rest/v1/tr/concepts/" + uuid;
        assertEquals(200, get(url).getStatusCode());
    }

    @Test
    public void verifyEditConceptSyncFromTR() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));

        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTerm;
        concept = dataStore.conceptForDiagnosis;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage().goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        concept = dataStore.conceptForDiagnosisEdit;
        page.goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndEditConcept(concept).editConcept(concept);


        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }

    @Test
    public void verifySyncOfFindingFromTR() {
        System.out.println("tr tests");
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

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
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

        driver.get(WebDriverProperties.getProperty("trInternalURL"));

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

    @Test
    public void verifySyncOfNumericConceptsEditFromTR() {

        driver.get(WebDriverProperties.getProperty("trInternalURL"));
        ConceptData dataStore = new ConceptData();
        conceptReferenceTerm = dataStore.conceptReferenceTermForNumericConcept;
        concept = dataStore.conceptForNumericConcept;

        TRLoginPage page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "Admin123").goToAdministrationPage()
                .goToReferenceTermManagementPage().goToCreateReferenceTerm().createReferenceTerm(conceptReferenceTerm).goToTRAdministrationPage()
                .goToConceptDictionaryMaintenancePage().goToCreateNewConcept().createConcept(concept);

        concept = dataStore.conceptForNumericConceptEdit;
        page.goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndEditConcept(concept).editConcept(concept);


        driver.get(WebDriverProperties.getProperty("facilityOneOpenMRSInternalURL"));

        page = PageFactoryWithWait.initialize(driver, TRLoginPage.class);
        page.login("admin", "test").goToTRAdministrationPage().goToConceptDictionaryMaintenancePage().searchAndViewConceptWithWait(concept).readCurrentConcept(concept);
    }


    @After
    public void tearDown() {
        driver.quit();
    }


}
