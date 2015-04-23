package pages;

import domain.ChiefComplain;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.ArrayList;
import java.util.List;

public class DashBoardVisitPage extends Page {

    public WebDriver driver;


    public DashBoardVisitPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != (webDriver.findElement(By.className("visit-date")));
            }
        });

    }

    public DashBoardVisitPage verifyCheifComplainData(ChiefComplain expectedChiefComplain) {
        waitFindElement(By.xpath("//h2[text()='Observations']"));
        waitForMillis(2000);
        List<WebElement> chiefComplainListElements = driver.findElements(By.cssSelector(".value-text-only"));
        ArrayList<String> chiefComplainList = new ArrayList<String>();

        String expectedChiefComplainDetails = expectedChiefComplain.getChiefComplainName() + " since " + expectedChiefComplain.getDuration() + " " + expectedChiefComplain.getDurationUnit();

        for (WebElement chiefComplain : chiefComplainListElements) {

            chiefComplainList.add(chiefComplain.getText());
        }
        Assert.assertEquals("Can not find Chief Complain " + expectedChiefComplainDetails, true, chiefComplainList.contains(expectedChiefComplainDetails));
        System.out.println("Chief Complain Data verified for Patient: " + expectedChiefComplain.getChiefComplainName());
        return this;

    }

}
