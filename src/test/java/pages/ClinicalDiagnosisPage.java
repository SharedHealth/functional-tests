package pages;

import domain.Diagnosis;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ClinicalDiagnosisPage extends Page {

    public WebDriver driver;

    @FindBy(xpath = "//button[text()='ave']")
    private WebElement saveButton;


    public ClinicalDiagnosisPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }


    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.xpath("//span[text()='Order']")));
            }
        });

    }


    public void enterDiagnosisDetails(Diagnosis firstDiagnosis, Diagnosis secondDiagnosis) {


        WebElement diagnosis1Name = driver.findElement(By.xpath("(//input[@type='text'])[1]"));
        setText(diagnosis1Name, firstDiagnosis.getDiagnosisName());
        waitForMillis(1000);
        WebElement diagnosisOptions = driver.findElement(By.linkText(firstDiagnosis.getDiagnosisName()));
        diagnosisOptions.click();


        WebElement diagnosis1Order = driver.findElement(By.xpath("(//buttons-radio[@options='orderOptions'])[1]"));
        WebElement diagnosis1Certainty = driver.findElement(By.xpath("(//buttons-radio[@options='certaintyOptions'])[1]"));


        selectRadioButton(diagnosis1Order, firstDiagnosis.getOrder());
        selectRadioButton(diagnosis1Certainty, firstDiagnosis.getCertainty());

        WebElement diagnosis2Name = driver.findElement(By.xpath("(//input[@type='text'])[2]"));

        setText(diagnosis2Name, secondDiagnosis.getDiagnosisName());
        waitForMillis(1000);
        diagnosisOptions = driver.findElement(By.linkText(secondDiagnosis.getDiagnosisName()));
        diagnosisOptions.click();

        WebElement diagnosis2Order = driver.findElement(By.xpath("(//buttons-radio[@options='orderOptions'])[2]"));
        WebElement diagnosis2Certainty = driver.findElement(By.xpath("(//buttons-radio[@options='certaintyOptions'])[2]"));
        selectRadioButton(diagnosis2Order, secondDiagnosis.getOrder());
        selectRadioButton(diagnosis2Certainty, secondDiagnosis.getCertainty());
        saveButton.click();
        System.out.println("Diagnosis Data Saved for Patient.");

    }


}

