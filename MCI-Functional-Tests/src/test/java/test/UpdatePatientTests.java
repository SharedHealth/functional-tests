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
 * Created by ashutosh on 20/02/15.
 */
public class UpdatePatientTests extends TestSetup{

    PatientDataStore dataStore = new PatientDataStore();
    UpdatedPatientData updatedData= new UpdatedPatientData();
    protected Patient primaryPatient;
    protected Patient updatedPatient;
    JSONObject person;

    @Category(FunctionalTest.class)
    @Test
    public void verifyPatientUpdatedPersonalInfoApproved() throws InterruptedException, JSONException {


        updatedPatient = updatedData.updatedPatientInfo;
        primaryPatient= dataStore.defaultPatient;
        person = createPatientDataJsonToPost(primaryPatient);
        String hid=createPatient(person);
        System.out.println(hid);

        driver.get(WebDriverProperties.getProperty("MCI_URL"));
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.adminLogin().goToPatientSearchPage().goPatientDetailsPage(hid).EditPatientDetails().goToPatientPersonalInfo()
            .updatePatientPersonalInfo(primaryPatient, updatedPatient).goPatientDetailsPage(hid)
            .verifyUpdateHeaderInfo(primaryPatient).logout();

        page.approverLogin().goToPatientApprovalPage().selectCatchment(primaryPatient)
            .verifyPatientNeedsApproval(hid).goToPatientApprovalDetailPage(hid)
            .verifyPendingApprovalFields(primaryPatient,hid).verifyPendingApprovalOldValues(primaryPatient)
            .verifyPendingApprovalNewValues(updatedPatient).approveRequest(primaryPatient,hid).logout();


        page.adminLogin().searchByHID(hid).verifyApprovedPatientUpdatedDetails(primaryPatient, updatedPatient).logout();

    }

    @Category(FunctionalTest.class)
    @Test
    public void verifyPatientUpdatedPersonalInfoReject() throws InterruptedException, JSONException {


        updatedPatient = updatedData.updatedPatientInfo;
        primaryPatient= dataStore.defaultPatient;
        person = createPatientDataJsonToPost(primaryPatient);
        String hid=createPatient(person);
        System.out.println(hid);

        driver.get(WebDriverProperties.getProperty("MCI_URL"));
        LoginPage page = PageFactoryWithWait.initialize(driver, LoginPage.class);
        page.adminLogin().goToPatientSearchPage().goPatientDetailsPage(hid).EditPatientDetails().goToPatientPersonalInfo()
                .updatePatientPersonalInfo(primaryPatient, updatedPatient).goPatientDetailsPage(hid)
                .verifyUpdateHeaderInfo(primaryPatient).logout();

        page.approverLogin().goToPatientApprovalPage().selectCatchment(primaryPatient)
                .verifyPatientNeedsApproval(hid).goToPatientApprovalDetailPage(hid)
                .verifyPendingApprovalFields(primaryPatient,hid).verifyPendingApprovalOldValues(primaryPatient)
                .verifyPendingApprovalNewValues(updatedPatient).rejectRequest(primaryPatient,hid).logout();

        page.adminLogin().searchByHID(hid).verifyRejectPatientUpdatedDetails(primaryPatient, updatedPatient).logout();


    }


}
