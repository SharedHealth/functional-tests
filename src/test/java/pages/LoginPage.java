package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import static utils.PageFactoryWithWait.initialize;

public class LoginPage extends Page {
    @FindBy(id = "username")
    private WebElement userName;
    @FindBy(id = "password")
    private WebElement password;
    @FindBy(id = "location")
    private WebElement location;
    @FindBy(className = "confirm")
    private WebElement loginButton;


    public WebDriver webDriver;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("username"));
            }
        });
    }
    public HomePage login(String uname) {

        userName.sendKeys(uname);
        password.sendKeys("Demo1234");
        loginButton.click();
        return initialize(webDriver, HomePage.class);
    }
    public HomePage login() {

        userName.sendKeys("Demo");
        password.sendKeys("Demo1234");
        Select loginLocation = new Select(location);
        loginLocation.selectByVisibleText("OPD-1");

        loginButton.click();
        return initialize(webDriver, HomePage.class);
    }

}
