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

   @FindBy(id="email")
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
                return null != webDriver.findElement(By.id("email"));
            }
        });

    }

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    public DashboardPage adminLogin(){
        String givenUser = WebDriverProperties.getProperty("MCI_USER_Admin");
        String givenPassword = WebDriverProperties.getProperty("MCI_USER_Admin_Password");
        userName.sendKeys(givenUser);
        password.sendKeys(givenPassword);
        loginButton.click();
        return initialize(webDriver, DashboardPage.class);

    }

    public DashboardPage approverLogin(){
        String givenUser = WebDriverProperties.getProperty("MCI_USER_Approver");
        String givenPassword = WebDriverProperties.getProperty("MCI_USER_Approver_Password");
        userName.sendKeys(givenUser);
        password.sendKeys(givenPassword);
        loginButton.click();
        return initialize(webDriver, DashboardPage.class);

    }


}
