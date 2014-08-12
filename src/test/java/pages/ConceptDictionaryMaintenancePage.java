package pages;

import domain.Concept;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import static utils.PageFactoryWithWait.initialize;

public class ConceptDictionaryMaintenancePage extends Page {

    @FindBy(linkText = "Add new Concept")
    private WebElement addNewConceptLink;

    @FindBy(id = "inputNode")
    private WebElement conceptSearchBox;



    public ConceptDictionaryMaintenancePage(WebDriver webDriver) {
        super(webDriver);
        this.webDriver = webDriver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.linkText("Add new Concept"));
            }
        });
    }

    public ConceptEditPage goToCreateNewConcept() {
        addNewConceptLink.click();
        return initialize(webDriver, ConceptEditPage.class);

    }

    public ConceptViewPage searchAndViewConceptWithWait(Concept concept) {

        System.out.println("Waiting 25 Secs for the Concept Sync to complete");
        waitForMillis(25000);
        setText(conceptSearchBox, concept.getName());
        WebElement conceptName = this.waitFindElement(By.xpath("//table[@id='openmrsSearchTable']//td//span"));
        conceptName.click();
        return initialize(webDriver, ConceptViewPage.class);
    }


    public ConceptEditPage searchAndEditConcept(Concept concept) {

        setText(conceptSearchBox, concept.getName());
        WebElement conceptName = this.waitFindElement(By.xpath("//table[@id='openmrsSearchTable']//td//span"));
        conceptName.click();
        WebElement editConceptLink = this.waitFindElement(By.id("editConcept"));
        editConceptLink.click();
        return initialize(webDriver, ConceptEditPage.class);
    }
}
