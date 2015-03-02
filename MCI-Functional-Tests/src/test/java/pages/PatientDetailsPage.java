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

    @FindBy(css = ".alert.alert-warning>ul")
    private WebElement updatedFields;

    @FindBy(linkText ="Patient approval list" )
    private WebElement patientApprovalLink;


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

        System.out.println(alertHeader.getText());
        assertEquals(alertHeader.getText(), patient.getGiven_name() + " " + patient.getSur_name() + " has updates for the following fields that are waiting for approval:");
        List<WebElement> fieldsName=updatedFields.findElements(By.tagName("li"));


        for(WebElement fields: fieldsName) {
            System.out.println(fields.getText());
            boolean checkFlag=false;

            if (fields.getText().equals(patient.getFieldGender()) || fields.getText().equals(patient.getFieldGiven_name()) || fields.getText().equals(patient.getFieldSur_name())){
                 checkFlag=true;
            }

            assertTrue( fields.getText() + " fields is not updated", checkFlag);

        }

        return initialize(webDriver, PatientDetailsPage.class);

    }

    public PatientApprovalPage goToPatientApprovalPage() {

        patientApprovalLink.click();

        return  initialize(webDriver, PatientApprovalPage.class);
    }
}
