package pages;

import domain.Patient;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import static utils.PageFactoryWithWait.initialize;

public class PatientDetailsPage extends Page {

    public WebDriver driver;

    @FindBy(id = "givenName")
    private WebElement patientFirstName;

    @FindBy(id = "familyName")
    private WebElement patientLastName;

    @FindBy(id = "gender")
    private WebElement patientGender;

    @FindBy(id = "birthdate")
    private WebElement patientDateOfBirth;

    @FindBy(id = "address1")
    private WebElement patientAddressLine1;

    @FindBy(id = "address3")
    private WebElement patientAddressUnion;

    @FindBy(id = "address5")
    private WebElement patientAddressUpaZilla;

    @FindBy(id = "countyDistrict")
    private WebElement patientAddressDistrict;

    @FindBy(id = "stateProvince")
    private WebElement patientAddressDivision;

    @FindBy(id = "National ID")
    private WebElement patientNID;

    @FindBy(id = "Health ID")
    private WebElement patientHID;

//    @FindBy(id = "education")
//    private WebElement patientEducation;

    @FindBy(id = "occupation")
    private WebElement patientOccupation;

//    @FindBy(id = "primaryContact")
//    private WebElement patientPrimaryContact;

    @FindBy(xpath = "//button[text()='Start OPD visit']")
    private WebElement startLabVisit;

    @FindBy(xpath = "//button[text()='Enter Visit Details']")
    private WebElement enterVisitDetails;


    public PatientDetailsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.id("National ID")));
            }
        });
    }

    public void verifyPatientDetails(Patient primaryPatient) {
        this.waitForMillis(1000);

//        Select patientEducationSelectBox = new  Select(patientEducation);
        Select patientGenderSelectBox = new Select(patientGender);
        Select patientOccupationSelectBox = new Select(patientOccupation);


        Assert.assertEquals(primaryPatient.given, patientFirstName.getAttribute("value"));
        Assert.assertEquals(primaryPatient.family, patientLastName.getAttribute("value"));
        Assert.assertEquals(primaryPatient.gender, patientGenderSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(primaryPatient.birthDate, patientDateOfBirth.getAttribute("value"));
        Assert.assertEquals(primaryPatient.address.getAddressLine1(), patientAddressLine1.getAttribute("value"));
        Assert.assertEquals(primaryPatient.address.getUnion(), patientAddressUnion.getAttribute("value"));
        Assert.assertEquals(primaryPatient.address.getUpazilla(), patientAddressUpaZilla.getAttribute("value"));
        Assert.assertEquals(primaryPatient.address.getDistrict(), patientAddressDistrict.getAttribute("value"));
        Assert.assertEquals(primaryPatient.address.getDivision(), patientAddressDivision.getAttribute("value"));
        Assert.assertEquals(primaryPatient.nid, patientNID.getAttribute("value"));
        Assert.assertNotNull(patientHID.getAttribute("value"));
//        Assert.assertEquals(primaryPatient.getEducation(),  patientEducationSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(primaryPatient.occupation, patientOccupationSelectBox.getFirstSelectedOption().getText());
//        Assert.assertEquals(primaryPatient.getPhoneNumber(),  patientPrimaryContact.getAttribute("value")) ;
        System.out.println("Patient with NID: " + primaryPatient.nid + " downloaded to Bahmni");


    }

    public PatientVisitInformationPage editPatientDetails(Patient primaryPatient) {
        this.waitForMillis(1000);

        setText(patientFirstName, primaryPatient.given);
        setText(patientLastName, primaryPatient.family);
        setText(patientAddressLine1, primaryPatient.address.getAddressLine1());
//        enterVisitDetails.click();
        patientNID.click();
        patientNID.sendKeys(Keys.RETURN);
        System.out.println("Patient with NID: " + primaryPatient.nid + " updated in Bahmni");
        return initialize(webDriver, PatientVisitInformationPage.class);


    }

    public PatientVisitInformationPage createPatient(Patient primaryPatient) {


//        Select patientEducationSelectBox = new  Select(patientEducation);
        Select patientGenderSelectBox = new Select(patientGender);
        Select patientOccupationSelectBox = new Select(patientOccupation);


        setText(patientFirstName, primaryPatient.given);
        setText(patientLastName, primaryPatient.family);
        setText(patientDateOfBirth, primaryPatient.birthDate);
        setText(patientAddressLine1, primaryPatient.address.getAddressLine1());
        if (primaryPatient.address.getUnion().equals("")) {
            setText(patientAddressUpaZilla, primaryPatient.address.getUpazilla());
            String addressOptions = "//a[text()='" + primaryPatient.address.getUpazilla() + ", " + primaryPatient.address.getDistrict() + "']";
            waitForMillis(1000);
            WebElement addressOption = driver.findElement(By.xpath(addressOptions));
            addressOption.click();
        } else {
            setText(patientAddressUnion, primaryPatient.address.getUnion());
            waitForMillis(1000);
            String addressOptions = "//a[text()='" + primaryPatient.address.getUnion() + ", " + primaryPatient.address.getCityCorporation() + "']";
            WebElement addressOption = driver.findElement(By.xpath(addressOptions));
            addressOption.click();


        }
        setText(patientNID, primaryPatient.nid);
//        setText(patientPrimaryContact,primaryPatient.getPhoneNumber());

//        patientEducationSelectBox.selectByVisibleText(primaryPatient.getEducation());
        patientGenderSelectBox.selectByVisibleText(primaryPatient.gender);
        patientOccupationSelectBox.selectByVisibleText(primaryPatient.occupation);
        startLabVisit.click();
        System.out.println("Patient Created with NID: " + primaryPatient.nid);
        return initialize(webDriver, PatientVisitInformationPage.class);


    }


    public PatientVisitInformationPage startVisit(Patient primaryPatient) {

        this.waitForMillis(1000);

        patientNID.click();
        patientNID.sendKeys(Keys.RETURN);

        return initialize(webDriver, PatientVisitInformationPage.class);

    }
}
