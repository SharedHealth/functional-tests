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

    @FindBy(className = "save-consultation")
    private WebElement saveButton;

    @FindBy(id = "Vitals")
    private WebElement vitalsSection;

    @FindBy(xpath = "//strong[text()='Family History']")
    private WebElement familyHistorySection;

    @FindBy(id = "History_and_Examination")
    private WebElement chiefComplainSection;

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
                return null != (webDriver.findElement(By.xpath("//div[@id='Vitals']")));
            }
        });

    }


    public void enterChiefComplainDetails(ChiefComplain firstChiefComplain, ChiefComplain secondChiefComplain, ChiefComplain nonCodedChiefComplain) {

        chiefComplainSection.click();
        waitForMillis(1000);

        enterChiefComplainData(firstChiefComplain, 1);
        enterChiefComplainData(secondChiefComplain, 2);
        enterChiefComplainData(nonCodedChiefComplain, 3);
        waitForMillis(1000);
        saveButton.click();
        waitForMillis(1000);
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
            WebElement addNewNonCodedChiefComplainCheckBox = webDriver.findElements(By.cssSelector(".accept-btn")).get(index);
            addNewNonCodedChiefComplainCheckBox.click();

        }

        WebElement chiefComplainDuration = driver.findElements(By.cssSelector(".duration-value")).get(index);
        setText(chiefComplainDuration, chiefComplain.getDuration());

        WebElement chiefComplainUnit = driver.findElements(By.cssSelector(".duration-unit")).get(index);
        Select chiefComplainUnitSelect = new Select(chiefComplainUnit);
        chiefComplainUnitSelect.selectByVisibleText(chiefComplain.getDurationUnit());

    }

    public void enterVitals(Vitals patientVitals) {
        vitalsSection.click();
        waitForMillis(1000);
        List<WebElement> vitalsList = driver.findElements(By.cssSelector(".form-field-group"));
        enterVitalObservation(vitalsList, "Systolic", patientVitals.getSystolicBloodPressure());
        enterVitalObservation(vitalsList, "Diastolic", patientVitals.getDiastolicBloodPressure());
        enterVitalObservation(vitalsList, "Pulse", patientVitals.getPulse());
        enterVitalObservation(vitalsList, "Temperature", patientVitals.getTemperature());
        saveButton.click();
        System.out.println("Vitals Data Entered for Patient.");

    }

    public void enterFamilyHistory(FamilyHistory familyHistory) {
        familyHistorySection.click();
        waitForMillis(1000);

        List<WebElement> familyHistoryList = driver.findElements(By.cssSelector(".form-field-group"));

        enterObservation(familyHistoryList, "Relationship Desc\nRelationship", familyHistory.getRelationShipName());
        selectFromFamilyHistoryAutoComplete(familyHistory.getRelationShipName());

        enterObservation(familyHistoryList, "Born On\nBorn On", familyHistory.getBornOnDate());
        enterObservation(familyHistoryList, "Onset Age\nOnset Age (years)", familyHistory.getOnsetAge());
        enterObservationNotes(familyHistoryList, "Relationship Notes\nRelationship Notes", familyHistory.getRelationshipNotes());

        enterObservation(familyHistoryList, "Relationship Diagnosis\nRelationship Diagnosis", familyHistory.getRelationshipDiagnosis());
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
                if(vital.findElements(By.tagName("input")).size()>0) {
                    WebElement vitalInput = vital.findElement(By.tagName("input"));
                    setText(vitalInput, observationValue);
                }
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

