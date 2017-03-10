package domain;

public class PatientForJSON {
  public String given_name;
  public String sur_name;
  public String gender;
  public String date_of_birth;
  public String nid;
  public PresentAddress present_address;

  public PatientForJSON() {
    this.present_address = new PresentAddress();
  }
}
