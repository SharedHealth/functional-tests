package tests;

import data.PatientData;
import domain.Patient;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class TempTests {

    static WebDriver driver = new FirefoxDriver();
    protected Patient primaryPatient;


    @Test @Ignore
    public void verifyPatientSearchByNID(){

        driver.get(WebDriverProperties.getProperty("facilityTwoURL"));

        primaryPatient = PatientData.defaultPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().searchPatientByNID(primaryPatient.getNid()).viewPatientDetails().verifyPatientDetails(primaryPatient);



    }
    @Test @Ignore
    public void generateTimeStamp(){

        String id = String.valueOf(System.currentTimeMillis() );
        System.out.println(id);
        id=id.substring(7);
        System.out.println(id);
    }


}
