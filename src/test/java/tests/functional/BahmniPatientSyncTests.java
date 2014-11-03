package tests.functional;

import categories.FunctionalTest;
import data.PatientData;
import domain.Patient;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.TestSetup;
import utils.WebDriverProperties;

@Category(FunctionalTest.class)
public class BahmniPatientSyncTests extends TestSetup{

    private String facilityOneInternalURL = WebDriverProperties.getProperty("facilityOneInternalURL");
    private String facilityTwoInternalURL = WebDriverProperties.getProperty("facilityTwoInternalURL");
    protected Patient primaryPatient;

    @Test
    public void verifyPatientSync() {

        driver.get(facilityOneInternalURL);

        PatientData dataStore = new PatientData();
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
                .searchPatientByNIDAndDownload(primaryPatient.getNid())
                .verifyPatientDetails(primaryPatient);

    }

    @Test
    public void verifySyncForUpdatePatient () {

        driver.get(facilityOneInternalURL);

        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;

        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.
                login()
                .goToRegistrationPage()
                .goToCreatePatientPage()
                .createPatient(primaryPatient)
                .logout();

        primaryPatient = dataStore.defaultPatientWithEditedName;
        driver.get(facilityOneInternalURL);

        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.
                login()
                .goToNationalRegistry()
                .searchPatientByNIDAndDownload(primaryPatient.getNid())
                .editPatientDetails(primaryPatient);

        driver.get(facilityOneInternalURL);
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page
                .login()
                .goToNationalRegistry()
                .searchPatientByNIDAndDownload(primaryPatient.getNid())
                .verifyPatientDetails(primaryPatient);

    }
}
