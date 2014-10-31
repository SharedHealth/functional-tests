package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class MCIPatientSearchPage extends Page{

    WebDriver driver;
    @FindBy(id = "patientId")
    private WebElement patientNID;


    public MCIPatientSearchPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.id("patientId")));
            }
        });


    }

    public PatientDetailsPage searchPatientByNIDAndDownload(String nid) {
        setText(patientNID,nid);
        patientNID.sendKeys(Keys.ENTER);
        WebElement downloadButton = this.waitFindElement(By.xpath("//button[@class='download-btn btn']")) ;
        System.out.println("Patient with HID: "+downloadButton.getAttribute("data-hid")+" found in National Registry.");
        downloadButton.click();
        return initialize(webDriver, PatientDetailsPage.class);

    }
}
