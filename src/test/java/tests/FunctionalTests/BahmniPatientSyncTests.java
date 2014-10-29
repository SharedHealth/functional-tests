package tests.FunctionalTests;

import categories.MciUiTest;
import data.PatientData;
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

@Category(MciUiTest.class)
public class BahmniPatientSyncTests {

    protected Patient primaryPatient;
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @Test
    public void verifyPatientSync() {

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));

        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.newPatient1;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
