package pages;

import domain.ChiefComplain;
import domain.FamilyHistory;
import domain.Vitals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static utils.PageFactoryWithWait.initialize;

public class ClinicalObservationsPage extends Page {

    public WebDriver driver;

    @FindBy(xpath = "//button[text()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//strong[text()='Vitals']")
    private WebElement vitalsSection;

    @FindBy(xpath = "//strong[text()='Family History']")
    private WebElement familyHistorySection;

    @FindBy(linkText = "Visit")
    private WebElement visitButton;



    public ClinicalObservationsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.xpath("//span[text()='Temperature']")));
            }
        });

    }


    public void enterChiefComplainDetails(ChiefComplain firstChiefComplain, ChiefComplain secondChiefComplain, ChiefComplain nonCodedChiefComplain) {


        enterChiefComplainData(firstChiefComplain, 1);
        enterChiefComplainData(secondChiefComplain, 2);
        enterChiefComplainData(nonCodedChiefComplain, 3);
        saveButton.click();
        System.out.println("Chief Complain Data Entered for Patient.");

    }

    private void enterChiefComplainData(ChiefComplain chiefComplain, int chiefComplainNumber) {

        int index = chiefComplainNumber - 1;

        WebElement chiefComplainName = driver.findElements(By.cssSelector(".concept-name")).get(index);
        setText(chiefComplainName, chiefComplain.getChiefComplainName());
        waitForMillis(1000);
        if (chiefComplain.isCodedChiefComplain()) {
            WebElement chiefComplainAutoCompleteOption = webDriver.findElement(By.linkText(chiefComplain.getChiefComplainName()));
            chiefComplainAutoCompleteOption.click();
        } else {
            WebElement addNewNonCodedChiefComplainCheckBox = webDriver.findElement(By.cssSelector(".add-new"));
            addNewNonCodedChiefComplainCheckBox.click();

        }

        WebElement chiefComplainDuration = driver.findElements(By.cssSelector("[type=number]")).get(index);
        setText(chiefComplainDuration, chiefComplain.getDuration());

        WebElement chiefComplainUnit = driver.findElements(By.cssSelector(".duration-label")).get(index);
        Select chiefComplainUnitSelect = new Select(chiefComplainUnit);
        chiefComplainUnitSelect.selectByVisibleText(chiefComplain.getDurationUnit());

    }

    public void enterVitals(Vitals patientVitals) {
//        vitalsSection.click();
//        waitForMillis(1000);
        List<WebElement> vitalsList = driver.findElements(By.cssSelector(".form-field-group"));
        enterVitalObservation(vitalsList, "Systolic (mm Hg)", patientVitals.getSystolicBloodPressure());
        enterVitalObservation(vitalsList, "Diastolic (mm Hg)", patientVitals.getDiastolicBloodPressure());
        enterVitalObservation(vitalsList, "Pulse (/min)", patientVitals.getPulse());
        enterVitalObservation(vitalsList, "Temperature", patientVitals.getTemperature());
        saveButton.click();
        System.out.println("Vitals Data Entered for Patient.");

    }

    public void enterFamilyHistory(FamilyHistory familyHistory) {
        familyHistorySection.click();
        waitForMillis(1000);

        List<WebElement> familyHistoryList = driver.findElements(By.cssSelector(".form-field-group"));

        enterObservation(familyHistoryList, "Relationship", familyHistory.getRelationShipName());
        selectFromFamilyHistoryAutoComplete(familyHistory.getRelationShipName());

        enterObservation(familyHistoryList, "Born On", familyHistory.getBornOnDate());
        enterObservation(familyHistoryList, "Onset Age (years)", familyHistory.getOnsetAge());
        enterObservationNotes(familyHistoryList, "Relationship Notes", familyHistory.getRelationshipNotes());

        enterObservation(familyHistoryList, "Relationship Diagnosis", familyHistory.getRelationshipDiagnosis());
        selectFromFamilyHistoryAutoComplete(familyHistory.getRelationshipDiagnosis());

        saveButton.click();
        System.out.println("Family History data Entered for Patient.");
    }

    private void selectFromFamilyHistoryAutoComplete(String value) {
        waitForMillis(1000);
        WebElement relationshipAutoCompleteOption = webDriver.findElement(By.linkText(value));
        Actions builder = new Actions(webDriver);
        Action seriesOfActions = builder.moveToElement(relationshipAutoCompleteOption).clickAndHold().release().build();
        seriesOfActions.perform();
    }

    private void enterVitalObservation(List<WebElement> observationList, String observationName, String observationValue) {
        for (WebElement vital : observationList) {
            if (vital.getText().startsWith(observationName)) {
                WebElement vitalInput = vital.findElement(By.tagName("input"));
                setText(vitalInput, observationValue);
            }
        }
    }
    private void enterObservation(List<WebElement> observationList, String observationName, String observationValue) {
        for (WebElement vital : observationList) {
            if (vital.getText().equals(observationName)) {
                WebElement vitalInput = vital.findElement(By.tagName("input"));
                setText(vitalInput, observationValue);
            }
        }
    }

    private void enterObservationNotes(List<WebElement> observationList, String observationName, String observationValue) {
        for (WebElement vital : observationList) {
            if (vital.getText().equals(observationName)) {
                WebElement vitalInput = vital.findElement(By.tagName("textarea"));
                setText(vitalInput, observationValue);
            }
        }
    }


    public ClinicalVisitPage goToVisitPage() {
        visitButton.click();
        return initialize(webDriver, ClinicalVisitPage.class);
    }
}

