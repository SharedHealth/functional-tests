package tests.functional;

import categories.FunctionalTest;
import data.PatientFactory;
import domain.Patient;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.TestSetup;
import utils.WebDriverProperties;


public class BahmniPatientSyncTests extends TestSetup{

    private String facilityOneInternalURL = WebDriverProperties.getProperty("facilityOneInternalURL");
    private String facilityTwoInternalURL = WebDriverProperties.getProperty("facilityTwoInternalURL");
    protected Patient primaryPatient;

    @Category(FunctionalTest.class)
    @Test
    public void verifyPatientSync() {

        driver.get(facilityOneInternalURL);

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.defaultPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.
                login()
                .goToRegistrationPage()
                .goToCreatePatientPage()
                .createPatient(primaryPatient)
                .logout();

        driver.get(facilityTwoInternalURL);
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);

        page.
                login()
                .goToNationalRegistry()
                .searchPatientByNIDAndDownload(primaryPatient.nid)
                .verifyPatientDetails(primaryPatient);

    }

    @Category(FunctionalTest.class)
    @Test
    public void verifySyncForUpdatePatient () {

        driver.get(facilityOneInternalURL);

        PatientFactory dataStore = new PatientFactory();
        primaryPatient = dataStore.defaultPatient;

        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.
                login()
                .goToRegistrationPage()
                .goToCreatePatientPage()
                .createPatient(primaryPatient)
                .logout();

        primaryPatient = dataStore.defaultPatientWithEditedName;
        driver.get(facilityTwoInternalURL);

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.
                login()
                .goToNationalRegistry()
                .searchPatientByNIDAndDownload(primaryPatient.nid)
                .editPatientDetails(primaryPatient)
                .logout();

        driver.get(facilityOneInternalURL);
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page
                .login()
                .goToNationalRegistry()
                .searchPatientByNIDAndDownload(primaryPatient.nid)
                .verifyPatientDetails(primaryPatient);

    }
}
