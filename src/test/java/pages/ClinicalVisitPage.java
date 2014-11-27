package pages;

import domain.ChiefComplain;
import domain.FamilyHistory;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.HashMap;
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

    public ClinicalVisitPage verifyCheifComplainData(ChiefComplain expectedChiefComplain) {
        waitFindElement(By.xpath("//h2[text()='Observations']"));

        List<WebElement> chiefComplainListElements = driver.findElements(By.cssSelector(".chief-notes"));
        ArrayList<String> chiefComplainList = new ArrayList<String>();

        String expectedChiefComplainDetails = expectedChiefComplain.getChiefComplainName() + " since " + expectedChiefComplain.getDuration() + " " + expectedChiefComplain.getDurationUnit() + " ";

        for (WebElement chiefComplain : chiefComplainListElements)
            chiefComplainList.add(chiefComplain.getText());

        Assert.assertEquals("Can not find Chief Complain " + expectedChiefComplainDetails, true, chiefComplainList.contains(expectedChiefComplainDetails));
        System.out.println("Chief Complain Data verified for Patient: " + expectedChiefComplain.getChiefComplainName());
        return this;

    }

    public void verifyFamilyHistoryData(FamilyHistory expectedFamilyHistory) {
        waitForMillis(1000);
        WebElement familyHistoryTable = webDriver.findElement(By.xpath("//table[contains(., 'Family History')]"));

        HashMap<String, String> familyHistory = new HashMap<String, String>();
        List<WebElement> familyHistoryNames = familyHistoryTable.findElements(By.cssSelector(".name"));
        List<WebElement> familyHistoryValues = familyHistoryTable.findElements(By.cssSelector(".chief-notes"));

        for (int i = 0; i < familyHistoryNames.size(); i++) {
            String name = familyHistoryNames.get(i).getText();
            String value = familyHistoryValues.get(i).getText().trim();
            familyHistory.put(name, value);
        }

        Assert.assertEquals("Relationship", expectedFamilyHistory.getRelationShipDisplayText(), familyHistory.get("Relationship"));
        Assert.assertEquals("Born On", expectedFamilyHistory.getBornOnDateDisplayText(), familyHistory.get("Born On"));
        Assert.assertEquals("Onset Age", expectedFamilyHistory.getOnsetAge() + " years", familyHistory.get("Onset Age"));
        Assert.assertEquals("Relationship Notes", expectedFamilyHistory.getRelationshipNotes(), familyHistory.get("Relationship Notes"));
        Assert.assertEquals("Relationship Diagnosis", expectedFamilyHistory.getRelationshipDiagnosisDisplayText(), familyHistory.get("Relationship Diagnosis"));

        System.out.println("Family History data verified for Patient");

    }
}
