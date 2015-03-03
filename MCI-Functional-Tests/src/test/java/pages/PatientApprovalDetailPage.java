package pages;

import domain.Patient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 02/03/15.
 */
public class PatientApprovalDetailPage extends Page{

    @FindBy(css="#page-wrapper>div>h4")
    private WebElement header;

    @FindBy(id="searchHidButton")
    private WebElement searchHidButton;

    @FindBy(css=".form-control.hid")
    private WebElement enterHID;

    public PatientApprovalDetailPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {



    }


    public PatientApprovalDetailPage verifyPendingApprovalFields(Patient patient, String hid){

        System.out.println(header.getText());

        List<WebElement> allFields=webDriver.findElements(By.cssSelector(".table>tbody>tr>th"));

        assertEquals(header.getText(), patient.getGiven_name()+" "+patient.getSur_name()+" ("+hid+")");
        assertTrue(patient.getFieldGender()+ " field is mark for approval", isValuePresent(allFields,patient.getFieldGender()));
        System.out.println(patient.getFieldGender()+ " field is mark for approval");
        assertTrue(patient.getFieldGiven_name()+ " field is mark for approval", isValuePresent(allFields, patient.getFieldGiven_name()));
        System.out.println(patient.getFieldGiven_name()+ " field is mark for approval");
        assertTrue(patient.getFieldSur_name()+ " field is mark for approval", isValuePresent(allFields,"Sur Name"));
        System.out.println(patient.getFieldSur_name()+ " field is mark for approval");

        return initialize(webDriver, PatientApprovalDetailPage.class);

    }

    public PatientApprovalDetailPage verifyPendingApprovalOldValues(Patient patient){

        List<WebElement> allOldValues=webDriver.findElements(By.cssSelector(".table>tbody>tr>td"));
        assertTrue("Male"+ " is old value of this patient", isValuePresent(allOldValues,"Male"));
        System.out.println("Male"+ " is old value of this patient");
        assertTrue(patient.getGiven_name()+ " is old value of this patient", isValuePresent(allOldValues,patient.getGiven_name()));
        System.out.println(patient.getGiven_name()+ " is old value of this patient");
        assertTrue(patient.getSur_name()+ " is old value of this patient", isValuePresent(allOldValues,patient.getSur_name()));
        System.out.println(patient.getSur_name()+ " is old value of this patient");

        return initialize(webDriver, PatientApprovalDetailPage.class);

    }

    public PatientApprovalDetailPage verifyPendingApprovalNewValues(Patient updatedpatient){

        List<WebElement> allNewValues=webDriver.findElements(By.cssSelector(".col-lg-6"));

        assertTrue(updatedpatient.getGender()+ " is new value of this patient which needs approval", isValuePresent(allNewValues,updatedpatient.getGender()));
        System.out.println(updatedpatient.getGender()+ " is new value of this patient which needs approval");
        assertTrue(updatedpatient.getGiven_name()+ " is new value of this patient which needs approval", isValuePresent(allNewValues,updatedpatient.getGiven_name()));
        System.out.println(updatedpatient.getGiven_name()+ " is new value of this patient which needs approval");
        assertTrue(updatedpatient.getSur_name()+ " is new value of this patient which needs approval", isValuePresent(allNewValues,updatedpatient.getSur_name()));
        System.out.println(updatedpatient.getSur_name()+ " is new value of this patient which needs approval");


        return initialize(webDriver, PatientApprovalDetailPage.class);

    }

    public PatientApprovalDetailPage approveRequest(Patient patient, String hid){

        acceptRequest("field_name=gender", hid);
        acceptRequest("field_name=given_name", hid);
        acceptRequest("field_name=sur_name", hid);

        return initialize(webDriver, PatientApprovalDetailPage.class);

    }

    public PatientApprovalDetailPage rejectRequest(Patient patient, String hid){

        rejectRequest("field_name=gender", hid);
        rejectRequest("field_name=given_name", hid);
        rejectRequest("field_name=sur_name", hid);

        return initialize(webDriver, PatientApprovalDetailPage.class);

    }

    public PatientDetailsPage searchByHID(String hid){

        setText(enterHID,hid);
        searchHidButton.click();

        return initialize(webDriver, PatientDetailsPage.class);

    }

    private void acceptRequest  (String fieldName, String hid) {




        String accept = "/accept/" + hid;
        List<WebElement> viewButton = webDriver.findElements(By.xpath("//a[contains(@href, '" + accept + "')]"));
        for (WebElement acceptButton : viewButton) {

            if (acceptButton.getAttribute("href").contains(fieldName)) {
                acceptButton.click();
                break;
            }
        }


    }

    private void rejectRequest  (String fieldName, String hid){

        String reject="/reject/"+hid;
        List<WebElement> viewButton= webDriver.findElements(By.xpath("//a[contains(@href, '"+reject+"')]"));
        for (WebElement rejectButton: viewButton){

            if(rejectButton.getAttribute("href").contains(fieldName)){
                rejectButton.click();
                break;
            }
        }

    }

    private boolean isValuePresent(List<WebElement> allFields, String fieldName){

        for(WebElement fieldText: allFields){

            if(fieldText.getText().equals(fieldName))
                return true;
        }


        return false;
    }
}
