package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import static org.junit.Assert.*;
import static utils.PageFactoryWithWait.initialize;

import java.util.List;

/**
 * Created by ashutosh on 26/02/15.
 */
public class PatientPendingApprovalPage extends Page {

    @FindBy(css = ".table.table-bordered>tbody>tr>td")
    private List<WebElement> allPatientList;

    @FindBy(linkText = "Next")
    private WebElement nextButton;


    public PatientPendingApprovalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitForPageToLoad() {

    }

    public PatientPendingApprovalPage verifyPatientNeedsApproval(String hid) {


        boolean isFound = false;


        for (WebElement patient : allPatientList) {


            if (patient.getText().equals(hid)) {

                System.out.println("Patient with " + hid + " needs approval found on approval page");
                isFound = true;
                break;
            }
        }

        outer:  while (isButtonEnabled()) {
            nextButton.click();
            for (WebElement patient : allPatientList) {

                if (patient.getText().equals(hid)) {

                    System.out.println("Patient with " + hid + " needs approval found on approval page");
                    isFound = true;
                    break outer;
                }
            }

        }

        assertTrue(hid + " is not found", isFound);
        return initialize(webDriver, PatientPendingApprovalPage.class);

    }

    public PatientApprovalDetailPage goToPatientApprovalDetailPage(String hid){

     WebElement viewButton= webDriver.findElement(By.xpath("//a[contains(@href, '"+hid+"')]"));

        viewButton.click();



        return initialize(webDriver, PatientApprovalDetailPage.class);
    }

    private boolean isButtonEnabled() {

        String nameOfButton = null;

        WebElement pager= webDriver.findElement(By.className("pager"));
        List<WebElement> bothButton=pager.findElements(By.tagName("a"));
        for(WebElement disabledButton: bothButton) {

            if (disabledButton.getAttribute("class").equals("disabled")) {
                nameOfButton = disabledButton.getText();
                break;
            }else{
                nameOfButton="BothEnabled";
            }
        }


            if (nameOfButton.equals("Previous")) {

                return true;
            } else if (nameOfButton.equals("Next")) {

                return false;

            } else{


                return true;
            }

   }

}