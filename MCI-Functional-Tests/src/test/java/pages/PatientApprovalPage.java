package pages;

import domain.Patient;
import org.apache.bcel.generic.Visitor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 20/02/15.
 */
public class PatientApprovalPage extends Page{

    @FindBy(linkText ="Search Patient" )
    private WebElement searchPatientPage;

    @FindBy(xpath = "//select[@name='catchment']")
    private WebElement catchment;

    @FindBy(xpath = "//input[@value='Submit']")
    private WebElement submitButton;

    @FindBy(css=".table.table-bordered>tbody>tr>td")
    private List<WebElement> allPatientList;

    public PatientApprovalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.name("catchment"));
            }
        });

    }

    public PatientSearchPage goToPatientSearchPage(){

        searchPatientPage.click();

        return initialize(webDriver, PatientSearchPage.class);
    }


    public PatientPendingApprovalPage selectCatchment(Patient patient){


        Select catchmentCode= new Select(catchment);
        catchmentCode.selectByVisibleText(patient.getCatchment());
        submitButton.click();
        return initialize(webDriver, PatientPendingApprovalPage.class);

    }


}
