package pages;

import domain.ChiefComplain;
import domain.Vitals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class ClinicalObservationsPage extends Page {

    public WebDriver driver;

    @FindBy(xpath = "//button[text()='Save']")
    private WebElement saveButton;

    @FindBy(xpath = "//strong[text()='Vitals']")
    private WebElement vitalsSection;


    public ClinicalObservationsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.cssSelector(".concept-name")));
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
        vitalsSection.click();
        waitForMillis(1000);
        List<WebElement> vitalsList = driver.findElements(By.cssSelector(".form-field-group"));
        enterVital(vitalsList, "Systolic BD (mm Hg)", patientVitals.getSystolicBloodPressure());
        enterVital(vitalsList, "Diastolic BD (mm Hg)", patientVitals.getDiastolicBloodPressure());
        enterVital(vitalsList, "Pulse-BD (/min)", patientVitals.getPulse());
        enterVital(vitalsList, "Temperature-BD", patientVitals.getTemperature());
        saveButton.click();
        System.out.println("Chief Complain Data Entered for Patient.");

    }

    private void enterVital(List<WebElement> vitalsList, String vitalName, String vitalValue) {
        for (WebElement vital : vitalsList) {
            if (vital.getText().equals(vitalName)) {
                WebElement vitalInput = vital.findElement(By.tagName("input"));
                setText(vitalInput, vitalValue);
            }

        }
    }
}

