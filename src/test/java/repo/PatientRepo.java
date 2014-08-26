package repo;


import domain.Patient;
import org.json.JSONException;
import org.json.JSONObject;

import static utils.HttpUtil.postJson;
import static utils.WebDriverProperties.getProperty;

public class PatientRepo {

    private final String createUrl;
    private final String userName;
    private final String password;

    public PatientRepo() {
        createUrl = getProperty("mciPatientCreateURL");
        userName = getProperty("mciUserName");
        password = getProperty("mciPassword");
    }

    public String create(Patient patient) {
        return postJson(createUrl, toJson(patient), userName, password);
    }

    private String toJson(Patient patient) {
        JSONObject person = new JSONObject();
        JSONObject present_address = new JSONObject();
        try {
            person.put("nid", patient.getNid());
            person.put("first_name", patient.getFirstName());
            person.put("last_name", patient.getLastName());
            person.put("date_of_birth", "2000-03-01");
            person.put("gender", "1");
            person.put("occupation", "02");
            person.put("edu_level", "02");
            person.put("fathers_first_name", patient.getPrimaryContact());
            present_address.put("address_line", patient.getAddress().getAddressLine1());
            present_address.put("division_id", "10");
            present_address.put("district_id", "09");
            present_address.put("upazilla_id", "18");
            present_address.put("city_corporation", "16");
            present_address.put("ward", "01");
            person.put("present_address", present_address);
            return person.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
