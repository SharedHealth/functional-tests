package pages;

import domain.Patient;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import static utils.PageFactoryWithWait.initialize;

public class PatientDetailsPage extends Page{

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

    @FindBy(id = "cityVillage")
    private WebElement patientAddressUnion;

    @FindBy(id = "address3")
    private WebElement patientAddressUpaZilla;

    @FindBy(id = "countyDistrict")
    private WebElement patientAddressDistrict;

    @FindBy(id = "stateProvince")
    private WebElement patientAddressDivision;

    @FindBy(id = "National ID")
    private WebElement patientNID;

    @FindBy(id = "Health ID")
    private WebElement patientHID;

    @FindBy(id = "education")
    private WebElement patientEducation;

    @FindBy(id = "occupation")
    private WebElement patientOccupation;

    @FindBy(id = "primaryContact")
    private WebElement patientPrimaryContact;


    @FindBy(xpath = "//button[text()='Start OPD visit']")
    private WebElement startLabVisit;



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

        Select patientEducationSelectBox = new  Select(patientEducation);
        Select patientGenderSelectBox = new  Select(patientGender);
        Select patientOccupationSelectBox = new  Select(patientOccupation);


        Assert.assertEquals(primaryPatient.getFirstName(), patientFirstName.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getLastName(), patientLastName.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getGender(), patientGenderSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(primaryPatient.getDateOfBirth(), patientDateOfBirth .getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getAddress().getAddressLine1(), patientAddressLine1.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getAddress().getUnion(), patientAddressUnion .getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getAddress().getUpazilla(), patientAddressUpaZilla.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getAddress().getDistrict(), patientAddressDistrict.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getAddress().getDivision(), patientAddressDivision.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getNid(), patientNID.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getHid(), patientHID.getAttribute("value")) ;
        Assert.assertEquals(primaryPatient.getEducation(),  patientEducationSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(primaryPatient.getOccupation(),  patientOccupationSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(primaryPatient.getPrimaryContact(),  patientPrimaryContact.getAttribute("value")) ;
        System.out.println("Patient with HID: "+ primaryPatient.getHid()+ " downloaded to Bahmni");



    }

    public PatientVisitInformationPage createPatient(Patient primaryPatient) {

        String addressOptions = "//a[text()='"+primaryPatient.getAddress().getUnion()+", "+primaryPatient.getAddress().getUpazilla()+"']";

        Select patientEducationSelectBox = new  Select(patientEducation);
        Select patientGenderSelectBox = new  Select(patientGender);
        Select patientOccupationSelectBox = new  Select(patientOccupation);


        setText(patientFirstName,primaryPatient.getFirstName());
        setText(patientLastName,primaryPatient.getLastName());
        setText(patientDateOfBirth,primaryPatient.getDateOfBirth());
        setText(patientAddressLine1,primaryPatient.getAddress().getAddressLine1());
        setText(patientAddressUnion,primaryPatient.getAddress().getUnion());
        waitForMillis(1000);
        WebElement addressOption = driver.findElement(By.xpath(addressOptions));
        addressOption.click();
        setText(patientNID,primaryPatient.getNid());
        setText(patientHID,primaryPatient.getHid());
        setText(patientPrimaryContact,primaryPatient.getPrimaryContact());

        patientEducationSelectBox.selectByVisibleText(primaryPatient.getEducation());
        patientGenderSelectBox.selectByVisibleText(primaryPatient.getGender());
        patientOccupationSelectBox.selectByVisibleText(primaryPatient.getOccupation());
        startLabVisit.click();
        System.out.println("Patient Created with HID: "+primaryPatient.getHid()+" NID: "+primaryPatient.getNid());
        return initialize(webDriver, PatientVisitInformationPage.class);



    }
}
