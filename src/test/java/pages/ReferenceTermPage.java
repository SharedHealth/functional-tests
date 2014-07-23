package pages;

import domain.ConceptReferenceTerm;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

public class ReferenceTermPage extends Page{

    @FindBy(name = "conceptReferenceTerm.code")
    private WebElement code;

    @FindBy(name = "conceptReferenceTerm.name")
    private WebElement name;

    @FindBy(name = "conceptReferenceTerm.conceptSource")
    private WebElement conceptSource;

    @FindBy(name = "conceptReferenceTerm.description")
    private WebElement description;

    @FindBy(name = "conceptReferenceTerm.version")
    private WebElement version;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement saveButton;


    public WebDriver webDriver;

    public ReferenceTermPage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }



    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.name("conceptReferenceTerm.code"));
            }
        });
    }

    public ReferenceTermPage createReferenceTerm(ConceptReferenceTerm conceptReferenceTerm) {

        Select conceptSourceSelectBox = new  Select(conceptSource);


        setText(code,conceptReferenceTerm.getCode());
        setText(name,conceptReferenceTerm.getName());
        setText(description,conceptReferenceTerm.getDescription());
        setText(version,conceptReferenceTerm.getVersion());

        conceptSourceSelectBox.selectByVisibleText(conceptReferenceTerm.getSource());
        saveButton.click();

        System.out.println("Concept Code :"+conceptReferenceTerm.getCode()+ " Concept Name :"+conceptReferenceTerm.getName()+"  created");
        return this;

    }
}
