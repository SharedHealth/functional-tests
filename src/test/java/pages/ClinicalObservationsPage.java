package pages;

import domain.ChiefComplain;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

public class ClinicalObservationsPage extends Page {

    public WebDriver driver;

    @FindBy(xpath = "//button[text()='Save']")
    private WebElement saveButton;


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

}

