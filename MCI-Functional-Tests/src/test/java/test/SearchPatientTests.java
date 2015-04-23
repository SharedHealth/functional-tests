package test;
import categories.FunctionalTest;
import data.PatientDataStore;
import data.UpdatedPatientData;
import domain.Patient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pages.LoginPage;
import utils.PageFactoryWithWait;
import utils.TestSetup;
import utils.WebDriverProperties;

import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 04/03/15.
 */
public class SearchPatientTests extends TestSetup{

    PatientDataStore dataStore = new PatientDataStore();
    UpdatedPatientData updatedData= new UpdatedPatientData();
    protected Patient primaryPatient;
    protected Patient updatedPatientPersonalinfo;
    JSONObject person;

    @Category(FunctionalTest.class)
    @Test

    public void verifyPatientSearch() throws InterruptedException, JSONException {


        updatedPatientPersonalinfo= updatedData.updatedPatientInfo;
        primaryPatient= dataStore.defaultPatient;
        person = createPatientDataJsonToPost(primaryPatient);
        String hid=createPatient(person);
        System.out.println(hid);

        driver.get(WebDriverProperties.getProperty("MCI_URL"));
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.adminLogin();

    }




}
