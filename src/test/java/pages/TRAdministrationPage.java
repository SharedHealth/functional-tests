package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class TRAdministrationPage  extends Page {

    @FindBy(linkText = "Manage Reference Terms")
    private WebElement referenceTermManagementLink;

    @FindBy(linkText = "View Concept Dictionary")
    private WebElement conceptDictionary;

    public WebDriver webDriver;

    public TRAdministrationPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }



    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.linkText("Manage Reference Terms"));
            }
        });
    }

    public ReferenceTermManagementPage goToReferenceTermManagementPage() {
        referenceTermManagementLink.click();
        return initialize(webDriver, ReferenceTermManagementPage.class);
    }

    public ConceptDictionaryMaintenancePage goToConceptDictionaryMaintenancePage() {
        conceptDictionary.click();
        return initialize(webDriver, ConceptDictionaryMaintenancePage.class);
    }
}
