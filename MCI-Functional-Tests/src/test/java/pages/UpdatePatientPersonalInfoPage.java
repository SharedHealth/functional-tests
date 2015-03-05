package pages;

import domain.Patient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 20/02/15.
 */
public class UpdatePatientPersonalInfoPage extends Page{

    public WebDriver driver;

    @FindBy(linkText = "Present Address")
    private WebElement presentAddress;

    @FindBy(linkText = "Permanent Address")
    private WebElement permanentAddress;

    @FindBy(linkText = "Phone Number")
    private WebElement phoneNumber;

    @FindBy(linkText = "Primary Contact Number")
    private WebElement primaryContactNumber;

    @FindBy(linkText = "Relations")
    private WebElement relations;


    @FindBy(id="mci_bundle_patientBundle_patients_save")
    private WebElement save;

    @FindBy(linkText= "Cancel")
    private WebElement cancel;


    public UpdatePatientPersonalInfoPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.xpath("//label[text()='Given Name']"));
            }
        });

    }


    public UpdatePatientPersentAddressPage goToPatientPersentAddressPage() {

        presentAddress.click();

        return  initialize(webDriver, UpdatePatientPersentAddressPage.class);

    }

    public UpdatePatientPermanentAddressPage goToPatientPermanentAddressPage() {

        permanentAddress.click();

        return  initialize(webDriver, UpdatePatientPermanentAddressPage.class);

    }

    public UpdatePatientPhoneNumberPage goToPatientPhoneNumberPage() {

        phoneNumber.click();

        return  initialize(webDriver, UpdatePatientPhoneNumberPage.class);

    }

    public UpdatePatientPrimaryContactNumberPage goToPatientPrimaryContactNumberPage() {

        primaryContactNumber.click();

        return  initialize(webDriver, UpdatePatientPrimaryContactNumberPage.class);

    }

    public UpdatePatientRelationsPage goToPatientRelationsPage() {

        relations.click();

        return  initialize(webDriver, UpdatePatientRelationsPage.class);

    }

    public UpdatePatientPage updatePatientPersonalInfo(Patient currentPatientInfo, Patient updatedPatientInfo) throws InterruptedException {

        List<WebElement> personalInfo= webDriver.findElements(By.cssSelector(".form-group"));
        enterUpdatedPatientData(personalInfo, currentPatientInfo.getFieldGiven_name(), updatedPatientInfo.getGiven_name());
        enterUpdatedPatientData(personalInfo, currentPatientInfo.getFieldSur_name(), updatedPatientInfo.getSur_name());
        selectUpdatedPatientData(personalInfo, currentPatientInfo.getFieldGender(), updatedPatientInfo.getGender());
        clickSave();
        Thread.sleep(2000);


        return initialize(webDriver, UpdatePatientPage.class);
    }



    public void clickSave(){

        save.click();
    }

    public void clickCancel(){

        cancel.click();
    }
}
