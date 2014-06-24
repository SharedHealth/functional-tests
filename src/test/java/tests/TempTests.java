package tests;

import builders.PatientData;
import builders.PatientDataBuilder;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class TempTests {

    static WebDriver driver = new FirefoxDriver();
    protected PatientDataBuilder primaryPatient;


    @Test
    public void verifyPatientSearchByNID(){

        driver.get(WebDriverProperties.getProperty("facilityOneURL"));

        primaryPatient = PatientData.defaultPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().searchPatientByNID(primaryPatient.getNid()).viewPatientDetails().verifyPatientDetails(primaryPatient);



    }
    @Test
    public void searchInNationalRegistry(){
        driver.get(WebDriverProperties.getProperty("facilityOneURL"));
        primaryPatient = PatientData.newPatient;
        primaryPatient.withNid("NIDA573228").withHid("HIDA573228").withFirstName("A573228");

        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }
    @Test
    public void generateTimeStamp(){

        String id = String.valueOf(System.currentTimeMillis() );
        System.out.println(id);
        id=id.substring(7);
        System.out.println(id);
    }


}
