package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class ReferenceTermManagementPage extends Page {

    @FindBy(linkText = "Add New Reference Term")
    private WebElement addNewReferenceTermLink;


    public WebDriver webDriver;

    public ReferenceTermManagementPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }



    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.linkText("Add New Reference Term"));
            }
        });
    }

    public ReferenceTermPage goToCreateReferenceTerm() {
        addNewReferenceTermLink.click();
        return initialize(webDriver, ReferenceTermPage.class);
    }
}
