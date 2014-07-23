package pages;

import domain.Concept;
import domain.ConceptReferenceTerm;
import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class EditConceptPage extends Page{

    @FindBy(name = "namesByLocale[en].name")
    private WebElement name;

    @FindBy(name = "shortNamesByLocale[en].name")
    private WebElement shortName;

    @FindBy(name = "descriptionsByLocale[en].description")
    private WebElement description;

    @FindBy(name = "concept.conceptClass")
    private WebElement conceptClass;

    @FindBy(name = "concept.datatype")
    private WebElement dataType;

    @FindBy(name = "concept.version")
    private WebElement version;

    @FindBy(name = "synonymsByLocale[en][0].name")
    private WebElement synonyms1;

    @FindBy(name = "synonymsByLocale[en][1].name")
    private WebElement synonyms2;

    @FindBy(xpath = "//input[@value='Save Concept']")
    private WebElement saveButton;

    @FindBy(xpath = "//input[@value='Add Synonym']")
    private WebElement addSynonym;

    @FindBy(xpath = "//input[@value='Add Mapping']")
    private WebElement addMapping;


    public EditConceptPage(WebDriver driver) {
        super(driver);
        this.webDriver = webDriver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("namesByLocale[en].name"));
            }
        });
    }



    public void createConcept(Concept concept) {

        Select conceptClassSelectBox = new  Select(conceptClass);
        Select dataTypeSelectBox = new  Select(dataType);


        setText(name,concept.getName());
        setText(shortName,concept.getShortName());
        setText(description,concept.getDescription());
        setText(version,concept.getVersion());

        conceptClassSelectBox.selectByVisibleText(concept.getConceptClass());
        dataTypeSelectBox.selectByVisibleText(concept.getDataType());

        addSynonym.click();
        setText(synonyms1,concept.getSynonyms1());

        addMapping.click();
        WebElement conceptMappingRelationship = webDriver.findElement(By.xpath("(//table[@id='conceptMapTable']//select)[1]"));
        WebElement conceptMappingSource  = webDriver.findElement(By.id("term[0].source"));
        WebElement conceptMappingCode  = webDriver.findElement(By.id("term[0].code"));
        WebElement conceptMappingName  = webDriver.findElement(By.id("term[0].name"));

        Select conceptRelationshipSelectBox = new  Select(conceptMappingRelationship);
        conceptRelationshipSelectBox.selectByVisibleText(concept.getConceptMappingRelationship());

        Select conceptSourceSelectBox = new  Select(conceptMappingSource);
        conceptSourceSelectBox.selectByVisibleText(concept.getConceptMappingSource());
        setText(conceptMappingCode, concept.getConceptMappingCode());
        waitForMillis(1000);
        WebElement addressOption = webDriver.findElement(By.xpath("(//span[@class='autocompleteresult'])[1]"));
        addressOption.click();
        saveButton.click();
        waitForMillis(1000);
        saveButton.click();

        System.out.println("Concept name :"+concept.getName()+" Created");

    }

    public void verifyConceptDetails(Concept concept) {

        WebElement conceptMappingRelationship = webDriver.findElement(By.xpath("(//table[@id='conceptMapTable']//select)[1]"));

        Select conceptClassSelectBox = new  Select(conceptClass);
        Select dataTypeSelectBox = new  Select(dataType);
        Select conceptRelationshipSelectBox = new  Select(conceptMappingRelationship);


        Assert.assertEquals(concept.getName(), name.getAttribute("value")) ;
        Assert.assertEquals(concept.getSynonyms1(), synonyms1.getAttribute("value")) ;
        Assert.assertEquals(concept.getShortName() , shortName.getAttribute("value"));
        Assert.assertEquals(concept.getDescription(), description.getAttribute("value")) ;
        Assert.assertEquals(concept.getConceptClass(),  conceptClassSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(concept.getDataType(),  dataTypeSelectBox.getFirstSelectedOption().getText());
        Assert.assertEquals(concept.getVersion(), version.getAttribute("value")) ;
        Assert.assertEquals(concept.getConceptMappingRelationship(),  conceptRelationshipSelectBox.getFirstSelectedOption().getText());
//        Assert.assertEquals(concept.get ation(),  patientOccupationSelectBox.getFirstSelectedOption().getText());
//        Assert.assertEquals(concept.get ryContact(),  patientPrimaryContact.getAttribute("value")) ;

        System.out.println("Verified Concept details in Bahmni for Concept :"+concept.getName());
    }
}
