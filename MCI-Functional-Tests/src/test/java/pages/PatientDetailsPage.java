package pages;

import domain.Patient;
import junit.framework.Assert;
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
 * Created by ashutosh on 20/02/15.
 */
public class PatientDetailsPage extends Page{

    @FindBy(linkText = "Edit Patient Details")
    private WebElement editPatientDetailButton;

    @FindBy(css = ".alert.alert-warning>h4")
    private WebElement alertHeader;

    @FindBy(css = ".page-header")
    private WebElement nameHeader;

    @FindBy(css = ".alert.alert-warning>ul")
    private WebElement updatedFields;

    @FindBy(css = ".col-lg-3")
    private List<WebElement> fieldsName;


    @FindBy(linkText ="Patient approval list" )
    private WebElement patientApprovalLink;

    @FindBy(css=".dropdown-toggle")
    private WebElement user;

    @FindBy(linkText = "Logout")
    private WebElement logout;


    public PatientDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.linkText("Edit Patient Details"));
            }
        });

    }


    public UpdatePatientPage EditPatientDetails() {

        editPatientDetailButton.click();

        return initialize(webDriver, UpdatePatientPage.class);



    }

    public PatientDetailsPage verifyUpdateHeaderInfo(Patient patient){

//        System.out.println(alertHeader.getText());
//        assertEquals(alertHeader.getText(), patient.getGiven_name() + " " + patient.getSur_name() + " has updates for the following fields that are waiting for approval:");
//        List<WebElement> fieldsName=updatedFields.findElements(By.tagName("li"));
//
//
//        for(WebElement fields: fieldsName) {
//            System.out.println(fields.getText());
//            boolean checkFlag=false;
//
//            if (fields.getText().equals(patient.getFieldGender()) || fields.getText().equals(patient.getFieldGiven_name()) || fields.getText().equals(patient.getFieldSur_name())){
//                 checkFlag=true;
//            }
//
//            assertTrue( fields.getText() + " fields is not updated", checkFlag);
//
//        }

        return initialize(webDriver, PatientDetailsPage.class);

    }

    public PatientApprovalPage goToPatientApprovalPage() {

        patientApprovalLink.click();

        return  initialize(webDriver, PatientApprovalPage.class);
    }

    public PatientDetailsPage verifyApprovedPatientUpdatedDetails(Patient patient, Patient updatedPatient){

        System.out.println(patient.getFieldGiven_name()+" "+ patient.getFieldSur_name()+" is "+nameHeader.getText());
        assertEquals(nameHeader.getText(), updatedPatient.getGiven_name() + " " + patient.getSur_name());
        System.out.println(patient.getFieldGiven_name()+ " and "+patient.getFieldSur_name()+" "+ " is updated from"
                + patient.getGiven_name()+" "+patient.getSur_name()+" to "+updatedPatient.getGiven_name()+" "+patient.getSur_name());
        assertTrue(patient.getFieldGender()+ " is not updated from " + patient.getGender()+ " to "+ updatedPatient.getGender()
                , isvaluePresent(patient.getFieldGender(), updatedPatient.getGender()));
        System.out.println(patient.getFieldGender()+ " is updated from "+ patient.getGender()+" to "+updatedPatient.getGender());
        return  initialize(webDriver, PatientDetailsPage.class);

    }

    public PatientDetailsPage verifyRejectPatientUpdatedDetails(Patient patient, Patient updatedPatient){

        System.out.println(patient.getFieldGiven_name()+" and "+ patient.getFieldSur_name()+" is "+nameHeader.getText());
        assertEquals(nameHeader.getText(), patient.getGiven_name() + " " + patient.getSur_name());
        System.out.println(patient.getFieldGiven_name()+ " and "+patient.getFieldSur_name()+" "+ " is not updated from "
                + patient.getGiven_name()+" "+patient.getSur_name()+" to "+updatedPatient.getGiven_name()+" "+updatedPatient.getSur_name());
        assertTrue(patient.getFieldGender()+ " is got updated from " + patient.getGender()+ " to "+ updatedPatient.getGender()
                , isvaluePresent(patient.getFieldGender(), "Male"));

        System.out.println(patient.getFieldGender()+ " is not updated from "+ patient.getGender()+" to "+updatedPatient.getGender());

        return  initialize(webDriver, PatientDetailsPage.class);
    }

    private boolean isvaluePresent(String fieldName, String value){

        for(WebElement field: fieldsName){

            if(field.getText().contains(fieldName)){
                WebElement fieldValue=field.findElement(By.tagName("p"));
                if(fieldValue.getText().contains(value));
                return true;
            }
        }

        return false;
    }

    public void logout() throws InterruptedException {

        user.click();
        Thread.sleep(1000);
        logout.click();


    }
}
