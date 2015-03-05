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
public class UpdatePatientPrimaryContactNumberPage extends Page{

    @FindBy(linkText = "Personal Information")
    private WebElement personalInformation;

    @FindBy(linkText = "Present Address")
    private WebElement presentAddress;

    @FindBy(linkText = "Permanent Address")
    private WebElement permanentAddress;

    @FindBy(linkText = "Phone Number")
    private WebElement phoneNumber;

    @FindBy(linkText = "Relations")
    private WebElement relations;

    @FindBy(id="mci_bundle_patientBundle_patients_save")
    private WebElement save;

    @FindBy(linkText= "Cancel")
    private WebElement cancel;

    public UpdatePatientPrimaryContactNumberPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.xpath("//label[text()='Number']"));
            }
        });

    }

    public UpdatePatientPersonalInfoPage goToPatientPersonalInfo() {

        personalInformation.click();

        return  initialize(webDriver, UpdatePatientPersonalInfoPage.class);

    }

    public UpdatePatientPresentAddressPage goToPatientPersentAddressPage() {

        presentAddress.click();

        return  initialize(webDriver, UpdatePatientPresentAddressPage.class);

    }

    public UpdatePatientPremanentAddressPage goToPatientPermanentAddressPage() {

        permanentAddress.click();

        return  initialize(webDriver, UpdatePatientPremanentAddressPage.class);

    }

    public UpdatePatientPhoneNumberPage goToPatientPhoneNumberPage() {

        phoneNumber.click();

        return  initialize(webDriver, UpdatePatientPhoneNumberPage.class);

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
