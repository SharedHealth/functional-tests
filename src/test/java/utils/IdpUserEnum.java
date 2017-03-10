package utils;

import java.util.List;

import static java.util.Arrays.asList;

public enum IdpUserEnum {
    FACILITY("dmishra@thoughtworks.com", "thoughtworks", "18549", "1c2a599423203f639dcdd8574ac5391dd67d21316ea30ee364c8a8787fb79dd3", asList("302607"), null),
    PROVIDER("monikar@thoughtworks.com", "thoughtworks", "18556", "af6d37106aa4d5fbda63c9c29b264dc4e3de6e35362fcc997659a18a58ce42ac", asList("302607"), null),
    DATASENSE("rappasam@thoughtworks.com", "thoughtworks", "18552", "b7aa1f4001ac4b922dabd6a02a0dabc44cf5af74a0d1b68003ce7ccdb897a1d2", asList("3026"), null),
    MCI_ADMIN("ashutoks@thoughtworks.com", "thoughtworks", "18557", null, asList("3026"), null),
    MCI_ADMIN_WITH_MCI_USER("mciAdminAndMciUser@thoughtworks.com", "thoughtworks", "18571", null, asList("3026"), null),
    MCI_APPROVER("ranjan@thoughtworks.com", "thoughtworks", "18559", null, asList("3026"), null),
    MCI_SYSTEM("sohamgh@thoughtworks.com", "thoughtworks", "18551", "8dee09178ee632b2453b772f6aba069aac2a1935dc53c0bae447851fa91b53fb", null, null),
    PATIENT("utsabban@thoughtworks.com", "thoughtworks", "18558", null, null, "98000101629"),
    PATIENT_JOURNAL(null, null, "18553", "36007186ea16c1590af00cd94c0d63ddf891476a93d8696341832a217fcdf0b9", null, null),
    SHR("mritunjd@thoughtworks.com", "thoughtworks", "18550", "c6e6fd3a26313eb250e1019519af33e743808f5bb50428ae5423b8ee278e6fa5", asList("302607"), null);

    private final String email;
    private final String password;
    private final String clientId;
    private final String authToken;
    private List<String> catchment;
    private String hid;

    IdpUserEnum(String email, String password, String clientId, String authToken, List<String> catchment, String hid) {
        this.email = email;
        this.password = password;
        this.clientId = clientId;
        this.authToken = authToken;
        this.catchment = catchment;
        this.hid = hid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public List<String> getCatchment() {
        return catchment;
    }

    public String getHid() {
        return hid;
    }
}
