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
public class PatientSearchPage extends Page{

    @FindBy(linkText ="Patient approval list" )
    private WebElement patientApprovalLink;

    @FindBy(linkText="Dashboard")
    private WebElement dashboard;

    @FindBy(css=".form-control.hid")
    private WebElement enterHID;

    @FindBy(id="searchHidButton")
    private WebElement searchHidButton;

    @FindBy(css=".dropdown-toggle")
    private WebElement user;

    @FindBy(linkText = "Logout")
    private WebElement logout;



    public PatientSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("searchButton"));
            }
        });

    }

    public PatientApprovalPage goToPatientApprovalPage() {

        patientApprovalLink.click();

        return  initialize(webDriver, PatientApprovalPage.class);
    }

    public DashboardPage goToDashboard(){

        dashboard.click();

        return  initialize(webDriver, DashboardPage.class);


    }

    public void searchByHID(String hid){

        setText(enterHID,hid);
        searchHidButton.click();

    }

    public void logout() throws InterruptedException {

        user.click();
        Thread.sleep(1000);
        logout.click();


    }

    public PatientDetailsPage goPatientDetailsPage(String hid) {

        setText(enterHID,hid);
        searchHidButton.click();

        return initialize(webDriver, PatientDetailsPage.class);
    }




}
