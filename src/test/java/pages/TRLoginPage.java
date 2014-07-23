package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class TRLoginPage extends Page {
    @FindBy(id = "username")
    private WebElement userName;
    @FindBy(id = "password")
    private WebElement password;
    @FindBy(xpath = "//input[@type='submit']")
    private WebElement loginButton;


    public WebDriver webDriver;

    public TRLoginPage(WebDriver webDriver) {
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

    public TRHomePage login(String user, String pwd) {

        userName.sendKeys(user);
        password.sendKeys(pwd);
        loginButton.click();
        return initialize(webDriver, TRHomePage.class);
    }

}
