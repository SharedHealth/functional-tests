package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 20/02/15.
 */
public class UpdatePatientPersentAddressPage extends Page{

    @FindBy(linkText = "Personal Information")
    private WebElement personalInformation;

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


    public UpdatePatientPersentAddressPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.xpath("//label[text()='Upazila']"));
            }
        });

    }

    public UpdatePatientPersonalInfoPage goToPatientPersonalInfo() {

        personalInformation.click();

        return  initialize(webDriver, UpdatePatientPersonalInfoPage.class);

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

    public void clickSave(){

        save.click();
    }

    public void clickCancel(){

        cancel.click();
    }


}