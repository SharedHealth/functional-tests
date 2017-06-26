package utils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.dstu3.model.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static config.EnvironmentConfiguration.*;
import static java.util.Arrays.asList;

public class BundleFactory {
    private static final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();
    private static final IParser xmlParser = FhirContext.forDstu3().newXmlParser();

    private static Encounter encounter;
    private static Composition composition;
    private static String trContextPath;

    public static String BundleWithConfidentialEncounter(String hid) {
        return xmlParser.encodeResourceToString(bundleWithEncounter(hid, Composition.DocumentConfidentiality.V));
    }

    private static Bundle bundleWithEncounter(String hid, Composition.DocumentConfidentiality confidentiality) {
        Date date = new Date();
        Map<String, String> property = config.property;
        String providerUrl = property.get(HRM_SERVER_BASE_URL_KEY) + "/providers/" + property.get(PROVIDER_ID_KEY) + ".json";

        String facilityUrl = property.get(HRM_SERVER_BASE_URL_KEY) + "/facilities/" + property.get(FACILITY_ID_KEY) + ".json";
        String patientUrl = property.get(MCI_SERVER_BASE_URL_KEY) + property.get(MCI_PATIENT_CONTEXT_PATH_KEY) + "/" + hid;

        trContextPath = property.get(TR_SERVER_BASE_URL_KEY) + "/openmrs/ws/rest/v1/tr/";
        Reference subject = new Reference()
                .setDisplay(hid)
                .setReference(patientUrl);

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setId(UUID.randomUUID().toString());
        Meta metadata = new Meta();
        metadata.setLastUpdated(date);
        bundle.setMeta(metadata);

        composition = createComposition(date, facilityUrl, subject, confidentiality);
        bundle.addEntry().setFullUrl(composition.getId()).setResource(composition);

        encounter = createEncounter(date, providerUrl, facilityUrl, subject);
        composition.setEncounter(new Reference(encounter.getId()));
        composition.addSection().addEntry().setReference(encounter.getId()).setDisplay("Encounter");
        bundle.addEntry().setFullUrl(encounter.getId()).setResource(encounter);

        return bundle;
    }

    private static Encounter createEncounter(Date date, String providerUrl, String facilityUrl, Reference subject) {
        Encounter encounter = new Encounter();
        String id = "urn:uuid:" + UUID.randomUUID();
        encounter.setId(id);
        encounter.setIdentifier(asList(new Identifier().setValue(id)));
        encounter.setStatus(Encounter.EncounterStatus.FINISHED);
        encounter.addType(new CodeableConcept().addCoding(new Coding()
                .setSystem(trContextPath + "vs/encounter-type")
                .setCode("OPD")
        ));
        encounter.setClass_(new Coding()
                .setSystem("http://hl7.org/fhir/v3/ActCode")
                .setCode("AMB")
        );
        encounter.setSubject(subject);
        encounter.addParticipant().setIndividual(new Reference(providerUrl));
        encounter.setServiceProvider(new Reference(facilityUrl));
        encounter.setPeriod(new Period().setStart(DateUtils.addSeconds(date, 10)));
        return encounter;
    }

    private static Composition createComposition(Date date, String facilityUrl, Reference subject, Composition.DocumentConfidentiality confidentiality) {
        Composition composition = new Composition();
        Coding coding = new Coding()
                .setSystem("http://hl7.org/fhir/vs/doc-typecodes")
                .setCode("51899-3")
                .setDisplay("Details Document");
        composition.setType(new CodeableConcept().addCoding(coding));
        composition.setStatus(Composition.CompositionStatus.FINAL);
        composition.setDate(date);
        composition.setTitle("Patient Clinical Encounter");
        String id = "urn:uuid:" + UUID.randomUUID();
        composition.setId(id);
        composition.setIdentifier(new Identifier().setValue(id));
        composition.setSubject(subject);
        composition.setAuthor(asList(new Reference(facilityUrl)));
        composition.setConfidentiality(confidentiality);
        return composition;
    }


    public static String BundleWithConditionEncounterForFever(String hid) {
        Bundle bundle = bundleWithEncounter(hid, Composition.DocumentConfidentiality.N);

        Condition condition = new Condition();
        condition.setContext(new Reference().setReference(encounter.getId()));
        condition.setSubject(encounter.getSubject());
        condition.setAsserter(encounter.getParticipantFirstRep().getIndividual());
        condition.addCategory().addCoding(new Coding()
                .setSystem(trContextPath + "vs/condition-category")
                .setCode("complaint")
        );
        condition.setClinicalStatus(Condition.ConditionClinicalStatus.ACTIVE);
        condition.setVerificationStatus(Condition.ConditionVerificationStatus.PROVISIONAL);
        String feverUuid = config.property.get(TR_FEVER_CONCEPT_UUID_KEY);
        condition.setCode(new CodeableConcept().addCoding(new Coding()
                .setSystem(trContextPath + "/concepts/" + feverUuid)
                .setCode(feverUuid)
                .setDisplay("Fever")
        ));
        condition.setOnset(new Period()
                .setStart(DateUtils.addDays(new Date(), -2))
                .setEnd(new Date())
        );
        String conditionId = "urn:uuid:" + UUID.randomUUID();
        condition.setId(conditionId);
        condition.addIdentifier(new Identifier().setValue(conditionId));

        bundle.addEntry().setFullUrl(conditionId).setResource(condition);
        composition.addSection().addEntry().setReference(conditionId).setDisplay("Complaint");
        return xmlParser.encodeResourceToString(bundle);
    }
}
