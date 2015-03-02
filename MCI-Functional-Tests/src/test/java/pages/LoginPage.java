package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import utils.WebDriverProperties;
import static utils.PageFactoryWithWait.initialize;
/**
 * Created by ashutosh on 20/02/15.
 */
public class LoginPage extends Page {

   @FindBy(id="name")
   private WebElement userName;
   @FindBy(id="password")
   private WebElement password;
   @FindBy(tagName="button")
   private WebElement loginButton;


    @Override
    public void waitForPageToLoad() {

        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("name"));
            }
        });

    }

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public PatientSearchPage login(){
        String givenUser = WebDriverProperties.getProperty("MCI_USER_NAME");
        String givenPassword = WebDriverProperties.getProperty("MCI_USER_PASSWORD");
        userName.sendKeys(givenUser);
        password.sendKeys(givenPassword);
        loginButton.click();
        return initialize(webDriver, PatientSearchPage.class);

    }


}
