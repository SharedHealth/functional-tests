package domain;

import com.jayway.restassured.http.ContentType;
import config.ConfigurationProperty;
import config.EnvironmentConfiguration;
import data.PatientFactory;
import org.json.JSONException;
import utils.IdpUserEnum;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static utils.IdentityLoginUtil.login;

public class BundleFactory {

    private static Condition condition;
    private static Composition composition;
    private static Encounter encounter;
    private static Date today;
    private static Bundle bundle;


    private static final ConfigurationProperty config = EnvironmentConfiguration.getEnvironmentProperties();

    public static String BundleWithConfidentialEncounter(String hid) {
        condition = new Condition();
        composition = new Composition();
        encounter = new Encounter();
        today = new Date();
        bundle = new Bundle();
        composition.setConfidentiality("Y");
        return createBundle(hid);
    }


    public static String BundleWithConditionEncounterForFever(String hid) {
        condition = new Condition();
        composition = new Composition();
        encounter = new Encounter();
        today = new Date();
        return createBundle(hid);


    }

    private static String createBundle(String hid) {
        String code_id = "d587c115-82f3-11e5-b875-0050568225ca";
        String code_name = "Fever";

        encounter.hid = hid;
        encounter.date = today;
        encounter.provider_id = config.property.get("provider_id");


        condition.hid = hid;
        condition.codeId = code_id;
        condition.encounterId = encounter.getIdentifier();
        condition.date = today;
        condition.provider_id = config.property.get("provider_id");
        condition.code_name = code_name;

        composition.hid = hid;
        composition.complaintId = condition.getIdentifier();
        composition.encounterId = encounter.getIdentifier();
        composition.date = today;
        Bundle bundle = new Bundle();

        Entry compositionEntry = new Entry(composition);
        bundle.addEntry(compositionEntry);


        Entry encounterEntry = new Entry(encounter);
        bundle.addEntry(encounterEntry);


        Entry conditionEntry = new Entry(condition);
        bundle.addEntry(conditionEntry);
        return bundle.asXML();
    }

    public static void main(String[] args) throws IOException, JSONException {
        String baseUrl = config.property.get("mci_registry");

        Patient firstPatient = PatientFactory.validPatientWithMandatoryInformation();
        String patientDetails = getPatientDetails(firstPatient);
        String firstHid = createPatient(patientDetails);

        Patient secondPatient = PatientFactory.validPatientWithAllInformation();
        String patientWithAllDetails = getPatientDetails(secondPatient);
        String secondHid = createPatient(patientWithAllDetails);

        Patient patientWithNid = PatientFactory.validPatientWithMandatoryInformation();
        patientWithNid.gender = "M";
        String patientWithNidDetails = new PatientCCDSJSONFactory(baseUrl).withValidJSON(patientWithNid);
        String onlyNidHid = createPatient(patientWithNidDetails);

        PatientsCollection patientsCollection = new PatientsCollection();
        patientsCollection.addPatient("duplicate",patientDetails, firstHid);
        patientsCollection.addPatient("common_uid_nid_and_binbrn",patientWithAllDetails, secondHid);
        patientsCollection.addPatient("common_nid_and_binbrn",patientDetails, firstHid);
        patientsCollection.addPatient("common_uid_and_binbrn",patientWithAllDetails, secondHid);
        patientsCollection.addPatient("common_uid_and_nid",patientWithAllDetails, secondHid);
        patientsCollection.addPatient("common_nid",patientWithNidDetails, onlyNidHid);
        patientsCollection.addPatient("common_uid",patientWithAllDetails, secondHid);
        patientsCollection.addPatient("common_binbrn",patientDetails, firstHid);

        String myCurrentDir = System.getProperty("user.dir");
        FileWriter fileWriter = new FileWriter(myCurrentDir + "/src/test/java/data/updatablePatient.json");
        fileWriter.write(patientsCollection.getPatientsList());
        fileWriter.close();
        System.out.println("successfull");
    }

    private static String getPatientDetails(Patient patient) {
        String baseUrl = config.property.get("mci_registry");
        patient.gender = "M";
        patient.binBRN = patient.nid+"0000";
        return new PatientCCDSJSONFactory(baseUrl).withValidJSON(patient);
    }

    private static String createPatient(String patientDetails) {
        String baseUrl = config.property.get("mci_registry");
        String IDP_SERVER_BASE_URL = config.property.get("idp_server_base_url");
        String patientContextPath = "/api/v1/patients";

        IdpUserEnum idpUser = IdpUserEnum.FACILITY;
        String accessToken = login(idpUser, IDP_SERVER_BASE_URL);

        return given().
            body(patientDetails)
            .header("X-Auth-Token", accessToken).
                header("From", idpUser.getEmail()).
                header("client_id", idpUser.getClientId())
            .header("Content-Type", "application/json")
            .post(baseUrl+patientContextPath)
            .then().statusCode(SC_CREATED)
            .contentType(ContentType.JSON).extract()
            .response().jsonPath().getString("id");

    }
}
