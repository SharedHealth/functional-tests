module.exports = function(hid, confidentiality) {

	var pretty_data = require('pretty-data').pd;
		var encounter_payload = "";
		var confidentiality = confidentiality || 'No';
		var environment = process.env.ENVIRONMENT || 'QA';

	if (environment == 'QA') {
		encounter_payload += encounter_payload = "";
		encounter_payload += "<?xml version='1.0' encoding='UTF-8'?>";
		encounter_payload += "<feed ";
		encounter_payload += "    xmlns='http://www.w3.org/2005/Atom'>  ";
		encounter_payload += "    <title>Encounter</title>  ";
		encounter_payload += "    <id>urn:05ec226b-10de-4129-ac48-f927e077bb3b</id>  ";
		encounter_payload += "    <updated>2015-03-12T10:35:54+05:30</updated>  ";
		encounter_payload += "    <author>    ";
		encounter_payload += "        <uri>http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json</uri>  ";
		encounter_payload += "    </author>  ";
		encounter_payload += "    <entry>    ";
		encounter_payload += "        <title>Composition</title>    ";
		encounter_payload += "        <id>urn:d2ed9da3-ed3f-4e12-ac69-e2252c885061</id>    ";
		encounter_payload += "        <updated>2015-03-12T10:36:40.187+05:30</updated>    ";
		encounter_payload += "        <content type='text/xml'>      ";
		encounter_payload += "            <Composition ";
		encounter_payload += "                xmlns='http://hl7.org/fhir'>        ";
		encounter_payload += "                <identifier>          ";
		encounter_payload += "                    <value value='urn:d2ed9da3-ed3f-4e12-ac69-e2252c885061'/>        ";
		encounter_payload += "                </identifier>        ";
		encounter_payload += "                <date value='2015-03-12T10:35:54+05:30'/>        ";
		encounter_payload += "                <status value='final'/>        ";

		if (confidentiality == 'Yes') {
			encounter_payload += "             <confidentiality> ";
			encounter_payload += "                   <system value='http://hl7.org/fhir/v3/Confidentiality'/> ";
			encounter_payload += "                    <code value='V'/> ";
			encounter_payload += "                   <display value='very restricted'/>";
			encounter_payload += "  			</confidentiality>";
		}



		encounter_payload += "                <subject>          ";
		encounter_payload += "                    <reference value='http://172.18.46.56:8081/api/v1/patients/" + hid + "'/>          ";
		encounter_payload += "                    <display value='" + hid + "'/>        ";
		encounter_payload += "                </subject>        ";
		encounter_payload += "                <author>          ";
		encounter_payload += "                    <reference value='http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json'/>        ";
		encounter_payload += "                </author>        ";
		encounter_payload += "                <encounter>          ";
		encounter_payload += "                    <reference value='urn:796eb99a-697b-4f09-829a-582ad84f2144'/>          ";
		encounter_payload += "                    <display value='Encounter'/>        ";
		encounter_payload += "                </encounter>        ";
		encounter_payload += "                <section>          ";
		encounter_payload += "                    <content>            ";
		encounter_payload += "                        <reference value='urn:796eb99a-697b-4f09-829a-582ad84f2144'/>            ";
		encounter_payload += "                        <display value='Encounter'/>          ";
		encounter_payload += "                    </content>        ";
		encounter_payload += "                </section>        ";
		encounter_payload += "                <section>          ";
		encounter_payload += "                    <content>            ";
		encounter_payload += "                        <reference value='urn:fbf10159-ec18-4fa7-90fa-fed6a03223d6'/>            ";
		encounter_payload += "                        <display value='Immunization'/>          ";
		encounter_payload += "                    </content>        ";
		encounter_payload += "                </section>      ";
		encounter_payload += "            </Composition>    ";
		encounter_payload += "        </content>  ";
		encounter_payload += "    </entry>  ";

		encounter_payload += "    <entry>    ";
		encounter_payload += "        <title>Encounter</title>    ";
		encounter_payload += "        <id>urn:796eb99a-697b-4f09-829a-582ad84f2144</id>    ";
		encounter_payload += "        <updated>2015-03-12T10:36:40.187+05:30</updated>    ";
		encounter_payload += "        <content type='text/xml'>      ";
		encounter_payload += "            <Encounter ";
		encounter_payload += "                xmlns='http://hl7.org/fhir'>        ";
		encounter_payload += "                <identifier>          ";
		encounter_payload += "                    <value value='urn:796eb99a-697b-4f09-829a-582ad84f2144'/>        ";
		encounter_payload += "                </identifier>        ";
		encounter_payload += "                <status value='finished'/>        ";
		encounter_payload += "                <class value='outpatient'/>        ";
		encounter_payload += "                <type>          ";
		encounter_payload += "                    <text value='Consultation'/>        ";
		encounter_payload += "                </type>        ";
		encounter_payload += "                <subject>          ";
		encounter_payload += "                    <reference value='http://172.18.46.56:8081/api/v1/patients/" + hid + "'/>          ";
		encounter_payload += "                    <display value='" + hid + "'/>        ";
		encounter_payload += "                </subject>        ";
		encounter_payload += "                <participant>          ";
		encounter_payload += "                    <individual>            ";
		encounter_payload += "                        <reference value='http://hrmtest.dghs.gov.bd/api/1.0/providers/24.json'/>          ";
		encounter_payload += "                    </individual>        ";
		encounter_payload += "                </participant>        ";
		encounter_payload += "                <indication>          ";
		encounter_payload += "                    <reference value='urn:796eb99a-697b-4f09-829a-582ad84f2144'/>          ";
		encounter_payload += "                    <display value='Encounter'/>        ";
		encounter_payload += "                </indication>        ";
		encounter_payload += "                <serviceProvider>          ";
		encounter_payload += "                    <reference value='http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json'/>        ";
		encounter_payload += "                </serviceProvider>      ";
		encounter_payload += "            </Encounter>    ";
		encounter_payload += "        </content>  ";
		encounter_payload += "    </entry>  ";


		encounter_payload += "    <entry>    ";
		encounter_payload += "        <title>Immunization</title>    ";
		encounter_payload += "        <id>urn:fbf10159-ec18-4fa7-90fa-fed6a03223d6</id>    ";
		encounter_payload += "        <updated>2015-03-12T10:36:40.227+05:30</updated>    ";
		encounter_payload += "        <content type='text/xml'>      ";
		encounter_payload += "            <Immunization ";
		encounter_payload += "                xmlns='http://hl7.org/fhir'>        ";
		encounter_payload += "                <identifier>          ";
		encounter_payload += "                    <value value='urn:fbf10159-ec18-4fa7-90fa-fed6a03223d6'/>        ";
		encounter_payload += "                </identifier>        ";
		encounter_payload += "                <date value='2015-02-12T00:00:00+05:30'/>        ";
		encounter_payload += "                <vaccineType>          ";
		encounter_payload += "                    <coding>            ";
		encounter_payload += "                        <system value='http://172.18.46.56:9080/openmrs/ws/rest/v1/tr/drugs/5799c579-3c78-4133-9e02-91c1006d862d'/>            ";
		encounter_payload += "                        <code value='5799c579-3c78-4133-9e02-91c1006d862d'/>            ";
		encounter_payload += "                        <display value='BCG'/>          ";
		encounter_payload += "                    </coding>        ";
		encounter_payload += "                </vaccineType>        ";
		encounter_payload += "                <subject>          ";
		encounter_payload += "                    <reference value='http://172.18.46.56:8081/api/v1/patients/" + hid + "'/>          ";
		encounter_payload += "                    <display value='" + hid + "'/>        ";
		encounter_payload += "                </subject>        ";
		encounter_payload += "                <reported value='true'/>        ";
		encounter_payload += "                <requester>          ";
		encounter_payload += "                    <reference value='http://hrmtest.dghs.gov.bd/api/1.0/providers/24.json'/>        ";
		encounter_payload += "                </requester>      ";
		encounter_payload += "            </Immunization>    ";
		encounter_payload += "        </content>  ";
		encounter_payload += "    </entry>";
		encounter_payload += "</feed>";
	}
	else
	{
		encounter_payload += "<feed xmlns='http://www.w3.org/2005/Atom'>"
		encounter_payload += "  <title>Encounter</title>"
		encounter_payload += "  <id>urn:810cd335-e95a-4722-9382-0ea204e1ebf1</id>"
		encounter_payload += "  <updated>2015-04-06T16:41:51+05:30</updated>"
		encounter_payload += "  <author>"
		encounter_payload += "    <uri>http://hrmtest.dghs.gov.bd/api/1.0/facilities/10019841.json</uri>"
		encounter_payload += "  </author>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Composition</title>"
		encounter_payload += "    <id>urn:5b37b064-ef56-49a9-b159-fa237ef8f538</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.586+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Composition xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:5b37b064-ef56-49a9-b159-fa237ef8f538'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <date value='2015-04-06T16:41:51+05:30'/>"
		encounter_payload += "        <status value='final'/>"

		if (confidentiality == 'Yes') {
			encounter_payload += "             <confidentiality> ";
			encounter_payload += "                   <system value='http://hl7.org/fhir/v3/Confidentiality'/> ";
			encounter_payload += "                    <code value='V'/> ";
			encounter_payload += "                   <display value='very restricted'/>";
			encounter_payload += "  			</confidentiality>";
		}

		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "        <author>"
		encounter_payload += "          <reference value='http://hrmtest.dghs.gov.bd/api/1.0/facilities/10019841.json'/>"
		encounter_payload += "        </author>"
		encounter_payload += "        <encounter>"
		encounter_payload += "          <reference value='urn:4f26e3d4-5e47-4e65-b3fb-23c1ff307724'/>"
		encounter_payload += "          <display value='Encounter'/>"
		encounter_payload += "        </encounter>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:4f26e3d4-5e47-4e65-b3fb-23c1ff307724'/>"
		encounter_payload += "            <display value='Encounter'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:6bc94b1c-d178-4e7a-8eff-404e9b35a117'/>"
		encounter_payload += "            <display value='Systolic'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:67dc7bed-04af-4856-8f34-9170e1ee9287'/>"
		encounter_payload += "            <display value='Diastolic'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:3d1435f7-483a-4259-bdc0-e348a562525d'/>"
		encounter_payload += "            <display value='Blood Pressure'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:949490b8-825d-4e3a-956f-60cf80dc6848'/>"
		encounter_payload += "            <display value='Pulse'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:90108427-a467-4bcd-a939-69586381ea8c'/>"
		encounter_payload += "            <display value='Temperature'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "        <section>"
		encounter_payload += "          <content>"
		encounter_payload += "            <reference value='urn:c960f633-9337-47b3-8840-313ba8f693ab'/>"
		encounter_payload += "            <display value='Vitals'/>"
		encounter_payload += "          </content>"
		encounter_payload += "        </section>"
		encounter_payload += "      </Composition>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Encounter</title>"
		encounter_payload += "    <id>urn:4f26e3d4-5e47-4e65-b3fb-23c1ff307724</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.586+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Encounter xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:4f26e3d4-5e47-4e65-b3fb-23c1ff307724'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <status value='finished'/>"
		encounter_payload += "        <class value='outpatient'/>"
		encounter_payload += "        <type>"
		encounter_payload += "          <text value='Consultation'/>"
		encounter_payload += "        </type>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "        <indication>"
		encounter_payload += "          <reference value='urn:4f26e3d4-5e47-4e65-b3fb-23c1ff307724'/>"
		encounter_payload += "          <display value='Encounter'/>"
		encounter_payload += "        </indication>"
		encounter_payload += "        <serviceProvider>"
		encounter_payload += "          <reference value='http://hrmtest.dghs.gov.bd/api/1.0/facilities/10019841.json'/>"
		encounter_payload += "        </serviceProvider>"
		encounter_payload += "      </Encounter>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Systolic</title>"
		encounter_payload += "    <id>urn:6bc94b1c-d178-4e7a-8eff-404e9b35a117</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.606+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Observation xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <name>"
		encounter_payload += "          <coding>"
		encounter_payload += "            <system value='http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/concepts/dd7d33d9-d7a5-4596-98fa-03a88ba631e1'/>"
		encounter_payload += "            <code value='dd7d33d9-d7a5-4596-98fa-03a88ba631e1'/>"
		encounter_payload += "            <display value='Systolic'/>"
		encounter_payload += "          </coding>"
		encounter_payload += "        </name>"
		encounter_payload += "        <valueDecimal value='120.5'/>"
		encounter_payload += "        <status value='final'/>"
		encounter_payload += "        <reliability value='ok'/>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:6bc94b1c-d178-4e7a-8eff-404e9b35a117'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "      </Observation>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Diastolic</title>"
		encounter_payload += "    <id>urn:67dc7bed-04af-4856-8f34-9170e1ee9287</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.606+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Observation xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <name>"
		encounter_payload += "          <coding>"
		encounter_payload += "            <system value='http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/concepts/7f6af303-e803-4d62-bbb4-e039ada4c837'/>"
		encounter_payload += "            <code value='7f6af303-e803-4d62-bbb4-e039ada4c837'/>"
		encounter_payload += "            <display value='Diastolic'/>"
		encounter_payload += "          </coding>"
		encounter_payload += "        </name>"
		encounter_payload += "        <valueDecimal value='80.3'/>"
		encounter_payload += "        <status value='final'/>"
		encounter_payload += "        <reliability value='ok'/>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:67dc7bed-04af-4856-8f34-9170e1ee9287'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "      </Observation>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Blood Pressure</title>"
		encounter_payload += "    <id>urn:3d1435f7-483a-4259-bdc0-e348a562525d</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.606+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Observation xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <name>"
		encounter_payload += "          <coding>"
		encounter_payload += "            <system value='http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/concepts/c8e87979-9198-490f-8afe-4673f810825d'/>"
		encounter_payload += "            <code value='c8e87979-9198-490f-8afe-4673f810825d'/>"
		encounter_payload += "            <display value='Blood Pressure'/>"
		encounter_payload += "          </coding>"
		encounter_payload += "        </name>"
		encounter_payload += "        <status value='final'/>"
		encounter_payload += "        <reliability value='ok'/>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:3d1435f7-483a-4259-bdc0-e348a562525d'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "        <related>"
		encounter_payload += "          <type value='has-component'/>"
		encounter_payload += "          <target>"
		encounter_payload += "            <reference value='urn:6bc94b1c-d178-4e7a-8eff-404e9b35a117'/>"
		encounter_payload += "          </target>"
		encounter_payload += "        </related>"
		encounter_payload += "        <related>"
		encounter_payload += "          <type value='has-component'/>"
		encounter_payload += "          <target>"
		encounter_payload += "            <reference value='urn:67dc7bed-04af-4856-8f34-9170e1ee9287'/>"
		encounter_payload += "          </target>"
		encounter_payload += "        </related>"
		encounter_payload += "      </Observation>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Pulse</title>"
		encounter_payload += "    <id>urn:949490b8-825d-4e3a-956f-60cf80dc6848</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.606+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Observation xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <name>"
		encounter_payload += "          <coding>"
		encounter_payload += "            <system value='http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/concepts/ac83596d-5bb9-43b0-8db6-e90fa2a6e1e9'/>"
		encounter_payload += "            <code value='ac83596d-5bb9-43b0-8db6-e90fa2a6e1e9'/>"
		encounter_payload += "            <display value='Pulse'/>"
		encounter_payload += "          </coding>"
		encounter_payload += "        </name>"
		encounter_payload += "        <valueDecimal value='79.0'/>"
		encounter_payload += "        <status value='final'/>"
		encounter_payload += "        <reliability value='ok'/>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:949490b8-825d-4e3a-956f-60cf80dc6848'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "      </Observation>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Temperature</title>"
		encounter_payload += "    <id>urn:90108427-a467-4bcd-a939-69586381ea8c</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.606+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Observation xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <name>"
		encounter_payload += "          <coding>"
		encounter_payload += "            <system value='http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/concepts/ed7401ef-c8ac-4af8-bf7c-1ed1d7147c78'/>"
		encounter_payload += "            <code value='ed7401ef-c8ac-4af8-bf7c-1ed1d7147c78'/>"
		encounter_payload += "            <display value='Temperature'/>"
		encounter_payload += "          </coding>"
		encounter_payload += "        </name>"
		encounter_payload += "        <valueDecimal value='98.3'/>"
		encounter_payload += "        <status value='final'/>"
		encounter_payload += "        <reliability value='ok'/>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:90108427-a467-4bcd-a939-69586381ea8c'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "      </Observation>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "  <entry>"
		encounter_payload += "    <title>Vitals</title>"
		encounter_payload += "    <id>urn:c960f633-9337-47b3-8840-313ba8f693ab</id>"
		encounter_payload += "    <updated>2015-04-06T16:42:46.606+05:30</updated>"
		encounter_payload += "    <content type='text/xml'>"
		encounter_payload += "      <Observation xmlns='http://hl7.org/fhir'>"
		encounter_payload += "        <name>"
		encounter_payload += "          <coding>"
		encounter_payload += "            <system value='http://172.18.46.53:9080/openmrs/ws/rest/v1/tr/concepts/970b1e8e-ecb7-4c79-aec8-d25b5c551a66'/>"
		encounter_payload += "            <code value='970b1e8e-ecb7-4c79-aec8-d25b5c551a66'/>"
		encounter_payload += "            <display value='Vitals'/>"
		encounter_payload += "          </coding>"
		encounter_payload += "        </name>"
		encounter_payload += "        <status value='final'/>"
		encounter_payload += "        <reliability value='ok'/>"
		encounter_payload += "        <identifier>"
		encounter_payload += "          <value value='urn:c960f633-9337-47b3-8840-313ba8f693ab'/>"
		encounter_payload += "        </identifier>"
		encounter_payload += "        <subject>"
		encounter_payload += "          <reference value='http://172.18.46.53:8081/api/v1/patients/" + hid + "'/>"
		encounter_payload += "          <display value='" + hid + "'/>"
		encounter_payload += "        </subject>"
		encounter_payload += "        <related>"
		encounter_payload += "          <type value='has-component'/>"
		encounter_payload += "          <target>"
		encounter_payload += "            <reference value='urn:3d1435f7-483a-4259-bdc0-e348a562525d'/>"
		encounter_payload += "          </target>"
		encounter_payload += "        </related>"
		encounter_payload += "        <related>"
		encounter_payload += "          <type value='has-component'/>"
		encounter_payload += "          <target>"
		encounter_payload += "            <reference value='urn:949490b8-825d-4e3a-956f-60cf80dc6848'/>"
		encounter_payload += "          </target>"
		encounter_payload += "        </related>"
		encounter_payload += "        <related>"
		encounter_payload += "          <type value='has-component'/>"
		encounter_payload += "          <target>"
		encounter_payload += "            <reference value='urn:90108427-a467-4bcd-a939-69586381ea8c'/>"
		encounter_payload += "          </target>"
		encounter_payload += "        </related>"
		encounter_payload += "      </Observation>"
		encounter_payload += "    </content>"
		encounter_payload += "  </entry>"
		encounter_payload += "</feed>"
	}
	return { details : encounter_payload,
			pretty_details : pretty_data.xml(encounter_payload) };

	
};	

		
