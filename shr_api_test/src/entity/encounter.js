module.exports = function(hid, confidentiality) {
var pretty_data = require('pretty-data').pd;
		var encounter_payload = "";
		var confidentiality = confidentiality || 'No';

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
	return { details : encounter_payload,
			pretty_details : pretty_data.xml(encounter_payload) };

	
};	

		
