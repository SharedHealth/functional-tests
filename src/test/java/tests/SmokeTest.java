package tests;

import builders.PatientData;
import builders.PatientDataBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.WebDriverProperties;

public class SmokeTest {

    static WebDriver driver ;
    protected PatientDataBuilder primaryPatient;

    @Before
    public void setUp(){
        driver = new FirefoxDriver();

    }

    @Test
    public void verifyPatientSync(){

        driver.get(WebDriverProperties.getProperty("facilityOneURL"));

        primaryPatient = PatientData.newPatient;
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToRegistrationPage().goToCreatePatientPage().createPatient(primaryPatient).logout();


        driver.get(WebDriverProperties.getProperty("facilityTwoURL"));
        page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.login().goToNationalRegistry().searchPatientByNID(primaryPatient.getNid()).verifyPatientDetails(primaryPatient);

    }

    @After
    public void tearDown(){
        driver.quit();
    }



}
