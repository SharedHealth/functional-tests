package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 20/02/15.
 */
public class DashboardPage extends Page{


    @FindBy(linkText ="Patient approval list" )
    private WebElement patientApprovalLink;

    @FindBy(linkText ="Search Patient" )
    private WebElement searchPatient;

    @FindBy(css=".form-control.hid")
    private WebElement enterHID;

    @FindBy(id="searchHidButton")
    private WebElement searchHidButton;

    @FindBy(css=".dropdown-toggle")
    private WebElement user;

    @FindBy(linkText = "Logout")
    private WebElement logout;


    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

    }

    public PatientApprovalPage goToPatientApprovalPage() {

        patientApprovalLink.click();

        return  initialize(webDriver, PatientApprovalPage.class);
    }

    public PatientSearchPage goToPatientSearchPage(){

        searchPatient.click();

        return initialize(webDriver, PatientSearchPage.class);

    }

    public PatientDetailsPage searchByHID(String hid){

        setText(enterHID,hid);
        searchHidButton.click();

        return initialize(webDriver, PatientDetailsPage.class);

    }
}
