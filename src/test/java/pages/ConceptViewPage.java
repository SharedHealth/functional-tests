package pages;

import domain.Concept;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;

public class ConceptViewPage extends Page{



    public ConceptViewPage(WebDriver driver) {
        super(driver);
        this.webDriver = webDriver;
    }

    @Override
    public void waitForPageToLoad() {
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return null != webDriver.findElement(By.id("newConcept"));
            }
        });
    }



    public void readCurrentConcept(Concept concept){
        List<WebElement> allConceptRowElements = webDriver.findElement(By.id("conceptTable")).findElements(By.tagName("tr"));

        HashMap<String,String> headerToDataMap = new HashMap<>();
        for (WebElement allConceptRowElement : allConceptRowElements) {
            if (!allConceptRowElement.getAttribute("id").equals("conceptMappingsHeadersRow")
                    && !allConceptRowElement.getAttribute("class").equals("evenRow") )
            {
            WebElement conceptRowHeader = allConceptRowElement.findElement(By.tagName("th"));
            String conceptRowHeaderText = conceptRowHeader.getText();
            String conceptRowDataText = allConceptRowElement.findElement(By.tagName("td")).getText();
            headerToDataMap.put(conceptRowHeaderText,conceptRowDataText);
                if(conceptRowHeaderText.equals("Version"))
                    break;
            }
        }

        List<WebElement> conceptMappingHeaderRowElements = webDriver.findElement(By.id("conceptMappingsHeadersRow")).findElements(By.tagName("th"));
        List<WebElement> conceptMappingDataRowElements = webDriver.findElement(By.className("evenRow")).findElements(By.tagName("td"));

        for (int counter = 0 ; counter < 4 ; counter++)
            headerToDataMap.put(conceptMappingHeaderRowElements.get(counter).getText(),conceptMappingDataRowElements.get(counter).getText());
//        System.out.println(headerToDataMap);

        Concept conceptForDiagnosisForVerification = new Concept.ConceptBuilder()
                .name(headerToDataMap.get("Fully Specified Name"))
                .synonyms1(headerToDataMap.get("Synonyms"))
                .shortName(headerToDataMap.get("Short Name"))
                .description(headerToDataMap.get("Description"))
                .conceptClass(headerToDataMap.get("Class"))
                .dataType(headerToDataMap.get("Datatype"))
                .version(headerToDataMap.get("Version"))
                .conceptMappingRelationship(headerToDataMap.get("Relationship"))
                .conceptMappingSource(headerToDataMap.get("Source"))
                .conceptMappingCode(headerToDataMap.get("Code"))
                .conceptMappingName(headerToDataMap.get("Name"))
                .build();

        Assert.assertEquals(concept.getName(), conceptForDiagnosisForVerification.getName());
        Assert.assertEquals(concept.getSynonyms1(),conceptForDiagnosisForVerification.getSynonyms1());
        Assert.assertEquals(concept.getShortName() ,conceptForDiagnosisForVerification.getShortName() );
//        Assert.assertEquals(concept.getDescription(),               conceptForDiagnosisForVerification.getDescription());
        Assert.assertEquals(concept.getConceptClass(),conceptForDiagnosisForVerification.getConceptClass());
        Assert.assertEquals(concept.getDataType(),conceptForDiagnosisForVerification.getDataType());
        Assert.assertEquals(concept.getVersion(), conceptForDiagnosisForVerification.getVersion());
        Assert.assertEquals(concept.getConceptMappingRelationship(),conceptForDiagnosisForVerification.getConceptMappingRelationship());
        Assert.assertEquals(concept.getConceptMappingSource(),conceptForDiagnosisForVerification.getConceptMappingSource());
        Assert.assertEquals(concept.getConceptMappingCode(),conceptForDiagnosisForVerification.getConceptMappingCode());
        Assert.assertEquals(concept.getConceptMappingName(),conceptForDiagnosisForVerification.getConceptMappingName());


    }

}
