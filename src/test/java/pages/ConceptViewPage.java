package pages;

import domain.Concept;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.HashMap;
import java.util.List;

public class ConceptViewPage extends Page {


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


    public void readCurrentConcept(Concept concept) {
        HashMap<String, String> headerDataMap = getHeaderDataMap();

        List<WebElement> conceptMappingHeaderRowElements = webDriver.findElement(By.id("conceptMappingsHeadersRow")).findElements(By.tagName("th"));
        List<WebElement> conceptMappingDataRowElements = webDriver.findElement(By.className("evenRow")).findElements(By.tagName("td"));

        for (int counter = 0; counter < 4; counter++)
            headerDataMap.put(conceptMappingHeaderRowElements.get(counter).getText(), conceptMappingDataRowElements.get(counter).getText());
//        System.out.println(headerToDataMap);

        Concept conceptForDiagnosisForVerification = new Concept.ConceptBuilder()
                .name(headerDataMap.get("Fully Specified Name"))
                .synonyms1(headerDataMap.get("Synonyms"))
                .shortName(headerDataMap.get("Short Name"))
                .description(headerDataMap.get("Description"))
                .conceptClass(headerDataMap.get("Class"))
                .dataType(headerDataMap.get("Datatype"))
                .version(headerDataMap.get("Version"))
                .conceptMappingRelationship(headerDataMap.get("Relationship"))
                .conceptMappingSource(headerDataMap.get("Source"))
                .conceptMappingCode(headerDataMap.get("Code"))
                .conceptMappingName(headerDataMap.get("Name"))
                .build();

        Assert.assertEquals(concept.getName(), conceptForDiagnosisForVerification.getName());
        Assert.assertEquals(concept.getSynonyms1(), conceptForDiagnosisForVerification.getSynonyms1());
        Assert.assertEquals(concept.getShortName(), conceptForDiagnosisForVerification.getShortName());
        Assert.assertEquals(concept.getConceptClass(), conceptForDiagnosisForVerification.getConceptClass());
        Assert.assertEquals(concept.getDataType(), conceptForDiagnosisForVerification.getDataType());
        Assert.assertEquals(concept.getVersion(), conceptForDiagnosisForVerification.getVersion());
        Assert.assertEquals(concept.getConceptMappingRelationship(), conceptForDiagnosisForVerification.getConceptMappingRelationship());
        Assert.assertEquals(concept.getConceptMappingSource(), conceptForDiagnosisForVerification.getConceptMappingSource());
        Assert.assertEquals(concept.getConceptMappingCode(), conceptForDiagnosisForVerification.getConceptMappingCode());
        Assert.assertEquals(concept.getConceptMappingName(), conceptForDiagnosisForVerification.getConceptMappingName());

        if(concept.getDataType().equalsIgnoreCase("Numeric"))
            verifyNumericRanges(concept);

        System.out.println("Concept name :" + concept.getName() + " found in Bahmni OpenMRS");


    }

    public String readCurrentConceptAttr(String attr) {
        return getHeaderDataMap().get(attr);
    }

    private HashMap<String, String> getHeaderDataMap() {
        List<WebElement> allConceptRowElements = webDriver.findElement(By.id("conceptTable")).findElements(By.tagName("tr"));

        HashMap<String, String> headerDataMap = new HashMap<>();
        for (WebElement allConceptRowElement : allConceptRowElements) {
            if (!allConceptRowElement.getAttribute("id").equals("conceptMappingsHeadersRow")     //Skip the Mapping Header Row
                    && !allConceptRowElement.getAttribute("class").equals("evenRow")             //Skip the Mapping Data Row
                    && !allConceptRowElement.getText().contains("range values are inclusive")) //Skip the Extra Cell
            {

                WebElement conceptRowHeader = allConceptRowElement.findElement(By.tagName("th"));
                String conceptRowHeaderText = conceptRowHeader.getText();
                String conceptRowDataText = allConceptRowElement.findElement(By.tagName("td")).getText();
                headerDataMap.put(conceptRowHeaderText, conceptRowDataText);
                if (conceptRowHeaderText.equals("Version"))
                    break;
            }
        }
        return headerDataMap;
    }

    private void verifyNumericRanges(Concept concept) {

        List<WebElement> allNumericRangeElements = webDriver.findElement(By.xpath("//table[@border=0]")).findElements(By.tagName("tr"));
        HashMap<String, String> headerToDataMap = new HashMap<>();
        for (WebElement allNumericRangeElement : allNumericRangeElements) {
            if ( !allNumericRangeElement.getText().contains("range values are inclusive")) //Skip the Extra Cell
            {
                WebElement NumericRangeHeader = allNumericRangeElement.findElement(By.tagName("th"));
                String numericRangeHeaderText = NumericRangeHeader.getText();

                String numericRangeDataText = allNumericRangeElement.findElement(By.tagName("td")).getText();
//                System.out.println(numericRangeHeaderText + "-" + numericRangeDataText);
                headerToDataMap.put(numericRangeHeaderText, numericRangeDataText);
                if (numericRangeHeaderText.equals("Precise?"))
                    break;
            }
        }

        Assert.assertEquals(concept.getNumericAbsoluteHigh(),headerToDataMap.get("Absolute High"));
        Assert.assertEquals(concept.getNumericCriticalHigh(),headerToDataMap.get("Critical High"));
        Assert.assertEquals(concept.getNumericNormalHigh(),headerToDataMap.get("Normal High"));
        Assert.assertEquals(concept.getNumericNormalLow(),headerToDataMap.get("Normal Low"));
        Assert.assertEquals(concept.getNumericCriticalLow(),headerToDataMap.get("Critical Low"));
        Assert.assertEquals(concept.getNumericAbsoluteLow(),headerToDataMap.get("Absolute Low"));
        Assert.assertEquals(concept.getNumericUnit(),headerToDataMap.get("F"));
    }

}
