package tests.functional;

import categories.MciUiTest;
import data.PatientData;
import domain.Patient;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.TestSetup;
import utils.WebDriverProperties;

@Category(MciUiTest.class)
public class BahmniPatientSyncTests extends TestSetup{

    protected Patient primaryPatient;


    @Test
    public void verifyPatientSync() {

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));

        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();

        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }

    @Test
    public void verifySyncForUpdatePatient () {

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));

        PatientData dataStore = new PatientData();
        primaryPatient = dataStore.defaultPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();

        primaryPatient = dataStore.defaultPatientWithEditedName;
        driver.get(WebDriverProperties.getProperty("facilityTwoInternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).editPatientDetails(primaryPatient);

        driver.get(WebDriverProperties.getProperty("facilityOneInternalURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNIDAndDownload(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }
}
