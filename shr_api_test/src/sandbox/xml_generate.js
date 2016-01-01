var builder = require('xmlbuilder');
var uuid = require('uuid');

var d = new Date();
var pad = function (n) { return n < 10 ? '0' + n : n; };
var getFormattedDate = function()
{
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + 'T' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds()) + "+0530"
}



//05ec226b-10de-4129-ac48-f927e077bb3b
//encounter_payload += "    <id>urn:05ec226b-10de-4129-ac48-f927e077bb3b</id>  ";
//encounter_payload += "    <updated>2015-03-12T10:35:54+05:30</updated>  ";
//encounter_payload += "    <author>    ";
//encounter_payload += "        <uri>http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json</uri>  ";
//encounter_payload += "    </author>  ";
//encounter_payload += "    <entry>    ";
//encounter_payload += "        <title>Composition</title>    ";
//encounter_payload += "        <id>urn:d2ed9da3-ed3f-4e12-ac69-e2252c885061</id>    ";
//encounter_payload += "        <updated>2015-03-12T10:36:40.187+05:30</updated>    ";
//encounter_payload += "        <content type='text/xml'>      ";
//encounter_payload += "            <Composition ";
//encounter_payload += "                xmlns='http://hl7.org/fhir'>        ";
//encounter_payload += "                <identifier>          ";
//encounter_payload += "                    <value value='urn:d2ed9da3-ed3f-4e12-ac69-e2252c885061'/>        ";
//encounter_payload += "                </identifier>        ";
//encounter_payload += "                <date value='2015-03-12T10:35:54+05:30'/>        ";
//encounter_payload += "                <status value='final'/>        ";


function xml_verify()
{
    var xml = builder.setupData("feed", { "xmlns" : 'http://www.w3.org/2005/Atom'})
        .ele("title","Encounter")
        .up()

        .ele("id", "urn:" + uuid.v4())
        .up()

        .ele("updated", getFormattedDate())
        .up()

        .ele("author")
        .ele("uri", "http://hrmtest.dghs.gov.bd/api/1.0/facilities/10000069.json")
        .up()
        .up()

        .ele("entry")
        .ele("title", "Composition")
        .up()

        .ele("id", "urn:" + uuid.v4())
        .up()

        .ele("updated", getFormattedDate())
        .up()

        .ele("content", {type : "'text/xml'"})
        .ele("Composition", {"xmlns" : "'http://hl7.org/fhir'"})
        .ele("identifier")
        .ele("value", {value : "'urn:" + uuid.v4() + "'"})
        .up()
        .up()
        .ele("date", {value : "'" + getFormattedDate() + "'"})
        .up()
        .ele("status", {value : "'final'"})

        .end({pretty : true});
        debugger;
        console.log(xml);
}


function Square()
{
    var root = builder.setupData("square");
    builder.com("Given below are the square numbers")
    for(var i = 1; i < 10; i++){
        var item= root.ele('data');
        item.att('x',i)
        //item.att('x^2', )
    }
}

xml_verify();