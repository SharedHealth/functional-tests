package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static utils.PageFactoryWithWait.initialize;

/**
 * Created by ashutosh on 20/02/15.
 */
public abstract class Page {

    private final long TIMEOUT_IN_SECONDS = 60L;
    protected Wait wait;
    protected WebDriver webDriver = null;

    public Page(WebDriver driver) {
        wait = new WebDriverWait(driver, TIMEOUT_IN_SECONDS);
        this.webDriver = driver;
    }

    public abstract void waitForPageToLoad();

    public Page waitForMillis(int millis) {
        try {
            Thread.sleep(millis);
            return this;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setText(WebElement webElement, String inputText) {
        if (inputText == null) return;
        webElement.clear();
        webElement.click();
        webElement.click();
        if (!inputText.isEmpty()) {
            webElement.sendKeys(inputText);
        } else {
            webElement.sendKeys(" ");
            webElement.clear();
        }
    }

    public void selectRadioButton(WebElement radioButtons, String valueToSelect) {

        List<WebElement> radioButtonOptions = radioButtons.findElements(By.tagName("button"));
        for (WebElement option : radioButtonOptions) {
            if (option.getText().equals(valueToSelect)) {
                option.click();

                break;
            }
        }
    }

    protected WebElement waitFindElementByXpath(String xPath) {
        return waitFindElement(By.xpath(xPath));
    }

    protected WebElement waitFindElement(By by) {
        long timeoutMs = TIMEOUT_IN_SECONDS * 1000;
        List<WebElement> w = webDriver.findElements(by);
        long timeSlice = 200; // milliseconds
        while (timeoutMs > 0 && w.isEmpty()) {
            timeoutMs -= timeSlice;
            try {
                Thread.sleep(timeSlice);
            } catch (InterruptedException e) {
            }
            w = webDriver.findElements(by);
        }

        if (w.isEmpty()) throw new NoSuchElementException("Element " + by.toString() + " not found!");

        return w.get(0);
    }

    protected void enterUpdatedPatientData(List<WebElement> updatedPatientInfo, String patientInfo, String patientValue){


        for(WebElement personalInfo: updatedPatientInfo) {
            if (personalInfo.getText().contains(patientInfo)){
                if (personalInfo.findElements(By.tagName("input")).size() > 0) {
                    WebElement patientInput = personalInfo.findElement(By.tagName("input"));
                    setText(patientInput, patientValue);
                }

            }

        }

    }

    protected  void selectUpdatedPatientData(List<WebElement> updatedPatientInfo, String patientInfo, String patientValue){

        for(WebElement personalInfo: updatedPatientInfo) {
            if (personalInfo.getText().startsWith(patientInfo)){
                if (personalInfo.findElements(By.tagName("select")).size() > 0) {
                    WebElement patientInput = personalInfo.findElement(By.tagName("select"));
                    Select selectValue= new Select(patientInput);
                    selectValue.selectByVisibleText(patientValue);

                }

            }

        }
    }



//    public TRAdministrationPage goToTRAdministrationPage() {
//
//        WebElement administrationLink = webDriver.findElement(By.linkText("Administration"));
//        administrationLink.click();
//        return initialize(webDriver, TRAdministrationPage.class);
//
//
//    }
//
//    public HomePage goToHomePage() {
//        WebElement homeLogo = webDriver.findElement(By.className("header-logo"));
//        homeLogo.click();
//        return initialize(webDriver, HomePage.class);
//
//    }
}
