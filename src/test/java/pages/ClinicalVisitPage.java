package pages;

import domain.ChiefComplain;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.List;

import static utils.PageFactoryWithWait.initialize;

public class ClinicalVisitPage extends Page {

    public WebDriver driver;

    @FindBy(linkText = "Diagnosis")
    private WebElement diagnosisButton;

    @FindBy(linkText = "Observations")
    private WebElement observationsButton;


    public ClinicalVisitPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.linkText("Visit")));
            }
        });

    }

    public ClinicalDiagnosisPage goToDiagnosisPage() {
        diagnosisButton.click();
        return initialize(driver, ClinicalDiagnosisPage.class);
    }

    public ClinicalObservationsPage goToObservationPage() {
        observationsButton.click();
        return initialize(driver, ClinicalObservationsPage.class);
    }

    public ClinicalVisitPage validateChiefComplainData(ChiefComplain expectedChiefComplain) {

        List<WebElement> chiefComplainListElements = driver.findElements(By.cssSelector(".chief-notes"));
        ArrayList<String> chiefComplainList = new ArrayList<String>();

        String expectedChiefComplainDetails = expectedChiefComplain.getChiefComplainName() + " since " + expectedChiefComplain.getDuration() + " " + expectedChiefComplain.getDurationUnit() + " ";

        for (WebElement chiefComplain : chiefComplainListElements)
            chiefComplainList.add(chiefComplain.getText());

        Assert.assertEquals("Can not find Chief Complain " + expectedChiefComplainDetails, true, chiefComplainList.contains(expectedChiefComplainDetails));
        System.out.println("Chief Complain Data verified for Patient: " + expectedChiefComplain.getChiefComplainName());
        return this;

    }
}
