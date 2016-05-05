var async = require('async');
var request = require('request');

var User = require('./../data/user');
var SSORequest = require('./../request/sso').SSORequest;
var EncounterRequest = require('./../request/encounter').EncounterRequest;
var CassandraClient = require('./../misc/cassandra_client').CassandraClient;

function Updations(from, to) {

    return {
        'from': from,
        'to': to
    };

}

function EncounterUpdater() {
    var facility_user = new User('provider');
    var user = new User('datasense');
    var updation_list = [
        new Updations('\"', '"'),
        new Updations("content>", "entry>"),
        new Updations("fullurl", "fullUrl"),
        new Updations("urn:", "urn:uuid:"),
        new Updations(":uuid:uuid:", ":uuid:"),
        new Updations("<id value=\"urn:uuid:", "<id value=\"")

    ];

    var cleanedUpEncounterData = function (encounterContents, filters) {
        var index = 0;
        var updatedEncounterContent = encounterContents;
        for (index = 0; index < updation_list.length; index++) {
            updatedEncounterContent = updatedEncounterContent.replace(new RegExp(updation_list[index].from,"gm"), updation_list[index].to);

        }
        return updatedEncounterContent;
    };

    var updateEncounter = function (result) {
        var result = [ { 'health_id' : '98000199037'  , 'encounter_id' : 'd0909e30-4868-4ca2-9029-e3a4b07f798d' }  ,
            { 'health_id' : '98000178692'  , 'encounter_id' : '08cdd899-d7eb-4854-904a-3b7c5fa377b5' }  ,
            { 'health_id' : '98000115231'  , 'encounter_id' : 'bec25f42-c936-4c44-b54b-4bdef16766bf' }  ,
            { 'health_id' : '98000151871'  , 'encounter_id' : '5b20a68f-b46d-411d-85c2-37aa2532407a' }  ,
            { 'health_id' : '98000147382'  , 'encounter_id' : 'c708551e-63a7-4c30-9ab4-87c97eab95a5' }  ,
            { 'health_id' : '98000132749'  , 'encounter_id' : '1e4b35fb-3617-4b32-9507-9352c7892e44' }  ,
            { 'health_id' : '98000117906'  , 'encounter_id' : '9f29b5f2-1d87-4357-b1a9-e4a5833429b4' }  ,
            { 'health_id' : '98000135783'  , 'encounter_id' : '561b35ca-5b8d-467f-95bb-f4fa382880a5' }  ,
            { 'health_id' : '98000109960'  , 'encounter_id' : 'baa620f0-3077-43bf-ba0b-e5d562254caf' }  ,
            { 'health_id' : '98000197916'  , 'encounter_id' : '3797a2ae-e9d8-4fb5-af5f-c8b8fd41d8cc' }  ,
            { 'health_id' : '98000136427'  , 'encounter_id' : '8ef293a9-d5dc-4658-b54d-f6558e98246e' }  ,
            { 'health_id' : '98000124373'  , 'encounter_id' : 'e387b384-d5ae-4048-8277-2053a7cb5095' }  ,
            { 'health_id' : '98000132749'  , 'encounter_id' : '4ca4d84c-0de5-4efb-9a73-bff233b7f981' }  ,
            { 'health_id' : '98000158025'  , 'encounter_id' : '6846bc7b-3c04-4cf4-afc8-50e345b37dcd' }  ,
            { 'health_id' : '98000128291'  , 'encounter_id' : 'd52c092f-cad5-43c4-9fc1-1815a599de82' }  ,
            { 'health_id' : '98000107360'  , 'encounter_id' : 'a692fbdd-6866-47b9-8d54-5ee10d8f7272' }  ,
            { 'health_id' : '98000126139'  , 'encounter_id' : '2025e41c-a6e4-4b52-85ec-60952322dcbc' }  ,
            { 'health_id' : '98000165947'  , 'encounter_id' : '2b3daef1-0ce0-4f35-a8c5-6a487c7b0dff' }  ,
            { 'health_id' : '98000139546'  , 'encounter_id' : '4a1345b4-ab9b-418e-97a3-f327711d00f2' }  ,
            { 'health_id' : '98000140270'  , 'encounter_id' : '75875c5f-bf13-4a68-846f-8567bf50d1fc' }  ,
            { 'health_id' : '98000180862'  , 'encounter_id' : 'b1c2c002-77ce-4ca7-addd-7b44af5bdd5f' }  ,
            { 'health_id' : '98000182488'  , 'encounter_id' : '4e7044a8-f734-4e9d-95fd-fd46f01cbcb3' }  ,
            { 'health_id' : '98000181910'  , 'encounter_id' : 'ae3d062d-5f94-4ca7-88dc-c2edf6ab1547' }  ,
            { 'health_id' : '98000190432'  , 'encounter_id' : '2535df33-329c-4b2f-9347-bf881fc8f046' }  ,
            { 'health_id' : '98000188253'  , 'encounter_id' : 'f5fbedbc-0bd2-44ba-a18c-180d777b3f4c' }  ,
            { 'health_id' : '98000151855'  , 'encounter_id' : 'f944d3ca-a898-44f8-ad7c-85910e777beb' }  ,
            { 'health_id' : '98000182306'  , 'encounter_id' : '5d3a9809-c910-4bf6-8613-ece958b6b642' }  ,
            { 'health_id' : '98000191299'  , 'encounter_id' : '1c676397-f260-43cd-92af-2da31dde3d02' }  ,
            { 'health_id' : '98000136583'  , 'encounter_id' : '03a68ba7-5bcd-49ff-ace4-b6cd6fe46284' }  ,
            { 'health_id' : '98000176670'  , 'encounter_id' : '6e68cb37-1b45-4784-98c1-dd06e61e5225' }  ,
            { 'health_id' : '98000191794'  , 'encounter_id' : 'c15feb3c-cfbc-41ed-8e29-15ca74cc47f6' }  ,
            { 'health_id' : '98000126949'  , 'encounter_id' : 'e16d5c31-16ce-435e-9e64-05460d2774ab' }  ,
            { 'health_id' : '98000193261'  , 'encounter_id' : '368c8761-1ed1-454e-83cc-2d862f948401' }  ,
            { 'health_id' : '98000125784'  , 'encounter_id' : '7a7de28f-7e0b-4acc-b41a-e5a92912e3c2' }  ,
            { 'health_id' : '98000136849'  , 'encounter_id' : '6dccbea1-3a67-4418-a8d3-5e39e3fb195f' }  ,
            { 'health_id' : '98000180169'  , 'encounter_id' : '965c8034-8f70-4fc2-9ff1-2a72b350971d' }  ,
            { 'health_id' : '98000124217'  , 'encounter_id' : '8fb8ff22-06e5-419d-bd0b-876c3802b57c' }  ,
            { 'health_id' : '98000125545'  , 'encounter_id' : '1878f926-9eec-4fc6-9873-ee1d785b2846' }  ,
            { 'health_id' : '98000146426'  , 'encounter_id' : '7a4ed3f9-13d0-4f63-8401-547891611e62' }  ,
            { 'health_id' : '98000156763'  , 'encounter_id' : 'a74ec64f-37ef-4370-989b-3845f524ec55' }  ,
            { 'health_id' : '98000166093'  , 'encounter_id' : 'bae07e64-ed69-40ae-a198-d65ff1135d2c' }  ,
            { 'health_id' : '98000126535'  , 'encounter_id' : '1c21433c-f397-41dc-8478-44a962feea63' }  ,
            { 'health_id' : '98000198831'  , 'encounter_id' : '41975aa6-8df9-482e-a1e5-12d238e422de' }  ,
            { 'health_id' : '98000128341'  , 'encounter_id' : 'b096c8d4-bdb9-4b4c-ac8e-79ec658d7ad1' }  ,
            { 'health_id' : '98000154263'  , 'encounter_id' : '7438e9b2-8ac1-447d-81b6-02eb000e1bdc' }  ,
            { 'health_id' : '98000134984'  , 'encounter_id' : '187d1488-a5b7-491e-9835-4e2ba46fcaee' }  ,
            { 'health_id' : '98000103054'  , 'encounter_id' : '9cde0c50-aa59-4613-860a-aee4f9804f65' }  ,
            { 'health_id' : '98000103054'  , 'encounter_id' : 'd03a00d2-a41a-466e-8746-6a8be278617a' }  ,
            { 'health_id' : '98000101769'  , 'encounter_id' : '523a064e-1c60-4a03-ab3e-f8a9bbbc64f3' }  ,
            { 'health_id' : '98000145105'  , 'encounter_id' : '7624d8c3-aae7-4a4a-ad27-1b53e7936337' }  ,
            { 'health_id' : '98000115215'  , 'encounter_id' : 'bff51a01-453d-4f5a-8ab9-fa66a5e50085' }  ,
            { 'health_id' : '98000142474'  , 'encounter_id' : '3efe849b-86ee-45eb-820a-b000a5c1b126' }  ,
            { 'health_id' : '98000160955'  , 'encounter_id' : '80f33983-ae24-4241-9c30-ca973dc52edb' }  ,
            { 'health_id' : '98000146046'  , 'encounter_id' : '34eb4713-4380-42b2-b6cb-81c755c4801e' }  ,
            { 'health_id' : '98000126154'  , 'encounter_id' : 'b008039b-8b63-4a39-bf04-18d66733c032' }  ,
            { 'health_id' : '98000185663'  , 'encounter_id' : '3fbff6d0-b105-4d29-88dc-e224251d5187' }  ,
            { 'health_id' : '98000155476'  , 'encounter_id' : '210979ba-dcd9-4008-900f-4e4413f39eee' }  ,
            { 'health_id' : '98000189905'  , 'encounter_id' : '0bca549c-7109-4794-99ab-39e9a6d50c6a' }  ,
            { 'health_id' : '98000125032'  , 'encounter_id' : '653c108f-b2f5-4d07-a2aa-9fe7ba0cff96' }  ,
            { 'health_id' : '98000141906'  , 'encounter_id' : 'd3238ee1-61c6-444a-9edd-cc41cf3ecdee' }  ,
            { 'health_id' : '98000142631'  , 'encounter_id' : 'd6d6b3c3-b7f8-40b4-91d2-3d85dace2bb2' }  ,
            { 'health_id' : '98000180813'  , 'encounter_id' : '7956b627-7bf3-4f26-b1f7-bacf15a40691' }  ,
            { 'health_id' : '98000179823'  , 'encounter_id' : '55a75314-8ea5-42b0-be6e-64b154eb92ba' }  ,
            { 'health_id' : '98000191299'  , 'encounter_id' : 'feaeea08-084c-4d94-ad76-9d624553afd6' }  ,
            { 'health_id' : '98000189715'  , 'encounter_id' : 'cac11c49-08d4-4bad-8359-342f52ecc427' }  ,
            { 'health_id' : '98000187420'  , 'encounter_id' : 'e11b2fd0-69f0-42fc-8eb4-306508cdc581' }  ,
            { 'health_id' : '98001000200'  , 'encounter_id' : 'f8f99990-1395-47ce-af3a-c3525a857a9e' }  ,
            { 'health_id' : '98000136195'  , 'encounter_id' : '118de3df-3583-495d-b7f6-6138714456fa' }  ,
            { 'health_id' : '98000197403'  , 'encounter_id' : '31def4c8-67bd-42a8-a486-70dfb0cec446' }  ,
            { 'health_id' : '98000173297'  , 'encounter_id' : '97a588a5-9498-4b7c-84bc-eed98dbe41f4' }  ,
            { 'health_id' : '98000123250'  , 'encounter_id' : '39c5c173-97c3-403f-bdbf-fdbeb13f1de6' }  ,
            { 'health_id' : '98000179492'  , 'encounter_id' : 'b177fb1e-2581-4306-a268-1e659e5d7728' }  ,
            { 'health_id' : '98000167133'  , 'encounter_id' : '8e8395fd-e69d-4636-8eca-9e88bab1ee49' }  ,
            { 'health_id' : '98000174220'  , 'encounter_id' : '061ad870-5da4-4fb8-8c7a-2b60fdb153c3' }  ,
            { 'health_id' : '98000176670'  , 'encounter_id' : '02ddcbd3-de10-442a-9718-43bffd3cadfa' }  ,
            { 'health_id' : '98000170392'  , 'encounter_id' : '32f6cd1d-6411-4c64-a250-be8455f2387e' }  ,
            { 'health_id' : '98000158413'  , 'encounter_id' : 'f8be3dfa-1676-48ec-8a9a-464043de3205' }  ,
            { 'health_id' : '98000131279'  , 'encounter_id' : 'fc1f2072-9ec6-4a53-ae15-f8035dbe5dbd' }  ,
            { 'health_id' : '98000159718'  , 'encounter_id' : '2a85173b-655d-48af-b281-8e5ce6595b75' }  ,
            { 'health_id' : '98000160211'  , 'encounter_id' : '0e090069-629d-4927-84dd-1d06cb3deabb' }  ,
            { 'health_id' : '98000196645'  , 'encounter_id' : '886b73c6-f1f6-4b0c-8cd9-552cec166300' }  ,
            { 'health_id' : '98000150022'  , 'encounter_id' : '8b32189c-647d-492b-abfe-405ff97c1660' }  ,
            { 'health_id' : '98000154230'  , 'encounter_id' : '7e58bcf5-748a-4b73-86a8-f7dbd6869e38' }  ,
            { 'health_id' : '98000125040'  , 'encounter_id' : 'dc13e9d3-bbff-4c32-9237-49615bc00f0a' }  ,
            { 'health_id' : '98000163652'  , 'encounter_id' : 'c79b2c9d-bf38-4e93-8cd3-3490e8f0ca87' }  ,
            { 'health_id' : '98000190572'  , 'encounter_id' : '6cf24b6d-a92a-41f3-97c1-0d4d73e218ea' }  ,
            { 'health_id' : '98001000507'  , 'encounter_id' : 'da59440d-2229-453a-af07-2cdcf3176a7a' }  ,
            { 'health_id' : '98000166804'  , 'encounter_id' : '05da6301-4dcd-4dcc-953d-4c8a1a26b4e9' }  ,
            { 'health_id' : '98000142631'  , 'encounter_id' : '6be3f8d8-1b5d-446b-af32-655606cbea01' }  ,
            { 'health_id' : '98000145626'  , 'encounter_id' : '7888638d-cfed-4097-8180-21e6b4459a7f' }  ,
            { 'health_id' : '98000117922'  , 'encounter_id' : '28d96f25-59d6-49b6-9520-0f06c712d1c5' }  ,
            { 'health_id' : '98000182306'  , 'encounter_id' : '7a7e0d3b-c977-4b83-9ffe-e7c5c64a6edc' }  ,
            { 'health_id' : '98000148661'  , 'encounter_id' : 'ae10560d-5f4e-4748-a70a-484faa5205be' }  ,
            { 'health_id' : '98000129919'  , 'encounter_id' : 'e9dbb5a8-a435-4e6c-9a67-6dd05fd8d5c6' }  ,
            { 'health_id' : '98000169337'  , 'encounter_id' : '0d7dc89b-8f0f-4dab-b253-ec5d1bc3543a' }  ,
            { 'health_id' : '98000174220'  , 'encounter_id' : 'fe0af04d-3ada-400f-aaf6-1cf6a0b2bf12' }  ,
            { 'health_id' : '98000107865'  , 'encounter_id' : 'ef36cb42-b07a-42f4-86d9-7813887d207c' }  ,
            { 'health_id' : '98000194491'  , 'encounter_id' : '18a26425-e2a5-457e-a632-4a975c758674' }  ,
            { 'health_id' : '98000124993'  , 'encounter_id' : 'aa3d189f-b7ff-4dcd-8da4-5faf23214e4a' }  ,
            { 'health_id' : '98000143043'  , 'encounter_id' : 'e70d0c61-61b8-4a49-bf5f-bebb05df241c' }  ,
            { 'health_id' : '98000143043'  , 'encounter_id' : 'c6798c58-4f7b-4d41-892c-e58b37330904' }  ,
            { 'health_id' : '98000156763'  , 'encounter_id' : 'c5eea01d-d198-4c7f-a764-684b76c82fda' }  ,
            { 'health_id' : '98000136682'  , 'encounter_id' : 'e0b7068b-5235-4dfa-8583-90f1d357f774' }  ,
            { 'health_id' : '98001001109'  , 'encounter_id' : '7ee4227e-f9b6-499d-8ca1-62f06358041d' }  ,
            { 'health_id' : '98000148232'  , 'encounter_id' : 'c8e5ca3e-6cb6-43e4-8f06-76a9815c53ae' }  ,
            { 'health_id' : '98000125032'  , 'encounter_id' : '0c3f4000-93a4-4203-95b4-83c3dd895a04' }  ,
            { 'health_id' : '98000191794'  , 'encounter_id' : 'fbb8e99a-a9e7-4aa7-b11a-58bfa2d4b8b2' }  ,
            { 'health_id' : '98000131295'  , 'encounter_id' : '10bc422b-62bc-41d5-8982-3afddd80e9f8' }  ,
            { 'health_id' : '98000133564'  , 'encounter_id' : 'e2337f23-8e2d-47d3-ae16-3c0609e01a9e' }  ,
            { 'health_id' : '98000178718'  , 'encounter_id' : 'a427b62f-c794-40d8-b2fe-14d268afc257' }  ,
            { 'health_id' : '98000177686'  , 'encounter_id' : '8de8d21e-6657-410b-86a3-8bc308536697' }  ,
            { 'health_id' : '98000128978'  , 'encounter_id' : '122dacae-3716-42c1-956f-8027ecb43a29' }  ,
            { 'health_id' : '98000199052'  , 'encounter_id' : '48b89994-09d7-40f7-8152-dabeb1f887a1' }  ,
            { 'health_id' : '98000189715'  , 'encounter_id' : 'b2aa4470-5a79-4284-89b2-7c6de0253f42' }  ,
            { 'health_id' : '98000138456'  , 'encounter_id' : '957ced5b-6600-4435-8836-7bc78c576dc7' }  ,
            { 'health_id' : '98000154768'  , 'encounter_id' : '7ee72129-77b6-4b40-83eb-7505bd941366' }  ,
            { 'health_id' : '98000171648'  , 'encounter_id' : '7e80752f-add6-4b91-9f0c-113d533ed782' }  ,
            { 'health_id' : '98000158413'  , 'encounter_id' : 'bcfd91f2-f20f-4ffb-af0f-465d7993ebfd' }  ,
            { 'health_id' : '98000198583'  , 'encounter_id' : '0c315666-1465-4533-b555-a695c1285485' }  ,
            { 'health_id' : '98000194012'  , 'encounter_id' : '802c1eda-a742-4978-b296-33b8e2bc72d9' }  ,
            { 'health_id' : '98000191802'  , 'encounter_id' : '9e427024-a539-48c3-ae7e-798972de814a' }  ,
            { 'health_id' : '98000180698'  , 'encounter_id' : 'f417f630-07f9-4083-a048-a5d2035d8491' }  ,
            { 'health_id' : '98000155302'  , 'encounter_id' : '4136965d-0707-4ecd-aadf-0ce21020d18e' }  ,
            { 'health_id' : '98000143241'  , 'encounter_id' : '0d748349-e45d-49f0-a708-9e2e87829be7' }  ,
            { 'health_id' : '98000180169'  , 'encounter_id' : '10790987-0247-4a0a-b4bf-a0c507e64a2b' }  ,
            { 'health_id' : '98000154263'  , 'encounter_id' : '4e04e282-de4b-40ea-9b17-722e8bdc41b4' }  ,
            { 'health_id' : '98000128820'  , 'encounter_id' : '361513ea-892e-43f9-accc-054750d3236c' }  ,
            { 'health_id' : '98000190655'  , 'encounter_id' : '53d4e98f-7e77-4f31-8773-efb272bf04c6' }  ,
            { 'health_id' : '98000175664'  , 'encounter_id' : 'b92f02f9-b84b-4f03-b230-4cd1ee4220c6' }  ,
            { 'health_id' : '98000134844'  , 'encounter_id' : '2dba123d-c8ce-4bf4-9e90-90e5d90e06fb' }  ,
            { 'health_id' : '98000105729'  , 'encounter_id' : 'c77c57f4-774d-40ff-bb07-6a1c187fecb4' }  ,
            { 'health_id' : '98000121726'  , 'encounter_id' : 'eff69ece-0d7e-4a97-be5a-18c4820ca6df' }  ,
            { 'health_id' : '98000165657'  , 'encounter_id' : 'e2665405-af7e-458e-964d-afce241366fa' }  ,
            { 'health_id' : '98000137326'  , 'encounter_id' : 'db01bbc3-8188-4649-8ee6-1921f579d326' }  ,
            { 'health_id' : '98000114176'  , 'encounter_id' : '703d6ebd-bf9b-43b4-b588-563e10dcb8e5' }  ,
            { 'health_id' : '98000116213'  , 'encounter_id' : 'c8f167f4-fdf3-4186-9dca-838861533a56' }  ,
            { 'health_id' : '98000195456'  , 'encounter_id' : '81ed4bb8-2914-48b8-a93e-ebcc70b9dc9b' }  ,
            { 'health_id' : '98000135676'  , 'encounter_id' : 'e9a5e0ee-6f74-4a2b-bfe2-169f5050b02a' }  ,
            { 'health_id' : '98000130966'  , 'encounter_id' : '0fb3505f-ca91-4ce2-a4e2-e06875dd453b' }  ,
            { 'health_id' : '98000105372'  , 'encounter_id' : '950faf00-c502-4b02-b3d6-b23c109d6cd9' }  ,
            { 'health_id' : '98000165236'  , 'encounter_id' : '831450dc-9599-4bfd-a6f1-50b8f70aa898' }  ,
            { 'health_id' : '98000141658'  , 'encounter_id' : 'ac17dc0b-8eb4-41e9-9b37-1ce2a75cc1af' }  ,
            { 'health_id' : '98000105372'  , 'encounter_id' : 'c0592820-e889-4f6d-a62c-e3fb0bf59c17' }  ,
            { 'health_id' : '98000105539'  , 'encounter_id' : '198d8b4b-a3a3-4861-bdf2-679f2128dd9b' }  ,
            { 'health_id' : '98000142474'  , 'encounter_id' : '35dcfcf0-ba31-4edc-bf80-c15740065e23' }  ,
            { 'health_id' : '98000131915'  , 'encounter_id' : 'aab16c40-7026-4461-8d88-81e2bfbbddd6' }  ,
            { 'health_id' : '98000107105'  , 'encounter_id' : '3e34a646-69ef-40fb-a3c7-10eade24a307' }  ,
            { 'health_id' : '98000199342'  , 'encounter_id' : 'a1bde79d-f1d7-4b99-bd44-c49e8168ae65' }  ,
            { 'health_id' : '98000155849'  , 'encounter_id' : '02190709-5943-4d0e-9847-baee36be9aa9' }  ,
            { 'health_id' : '98000169337'  , 'encounter_id' : '4caaba8b-b048-42a6-87f8-2308e74b592a' }  ,
            { 'health_id' : '98000126139'  , 'encounter_id' : 'd5f973d7-a221-45d9-b878-bb670430ea21' }  ,
            { 'health_id' : '98000113947'  , 'encounter_id' : '245baafb-cfe3-4b3d-a4df-58914bb13e22' }  ,
            { 'health_id' : '98000131295'  , 'encounter_id' : '56ec6deb-24c5-4497-853d-e88cb43b91b1' }  ,
            { 'health_id' : '98000167513'  , 'encounter_id' : '79a1276e-6523-4c94-92a4-ab36f401fc2a' }  ,
            { 'health_id' : '98000110067'  , 'encounter_id' : '21bf494b-d447-4dca-a9f6-f6fe22d19334' }  ,
            { 'health_id' : '98000124217'  , 'encounter_id' : '1d34b851-1f6c-4259-b778-62e457e1711c' }  ,
            { 'health_id' : '98000165947'  , 'encounter_id' : '4533cbad-2ceb-45ef-ab35-eda7c82cae48' }  ,
            { 'health_id' : '98000105729'  , 'encounter_id' : 'e7df1747-f021-4c2e-8493-a2b1d092b39a' }  ,
            { 'health_id' : '98000128697'  , 'encounter_id' : '97a19957-ca8d-4e6c-ab30-7006d7c9a1c4' }  ,
            { 'health_id' : '98000140213'  , 'encounter_id' : '2ed5bc7a-3412-4e4c-9859-d25a7dde96e5' }  ,
            { 'health_id' : '98000161268'  , 'encounter_id' : '7cd54b06-96f5-42b5-8562-a32f67701125' }  ,
            { 'health_id' : '98000109465'  , 'encounter_id' : 'd7671ff3-c13e-479e-8554-9edade2c4a64' }  ,
            { 'health_id' : '98000116346'  , 'encounter_id' : 'a739f4c1-f768-4ff3-b0c4-1aaf4335fdb7' }  ,
            { 'health_id' : '98000186554'  , 'encounter_id' : '151542ad-6fb7-4011-b5a0-8eb86db08ed8' }  ,
            { 'health_id' : '98000193261'  , 'encounter_id' : '3d97e506-94dc-418f-86db-c59f25d0bf75' }  ,
            { 'health_id' : '98000197254'  , 'encounter_id' : '3ae9b1eb-1b8c-464b-a901-58df2247901a' }  ,
            { 'health_id' : '98000166325'  , 'encounter_id' : 'cde57565-596f-497a-9529-103237572102' }  ,
            { 'health_id' : '98000167984'  , 'encounter_id' : '1ccfa37d-1596-498a-966d-fac0639fa6fb' }  ,
            { 'health_id' : '98000137029'  , 'encounter_id' : 'fb8fadf2-c3eb-497e-a8f9-71926fba5dce' }  ,
            { 'health_id' : '98000193774'  , 'encounter_id' : 'd1580a6b-be3c-475a-bc9a-417c120fdb57' }  ,
            { 'health_id' : '98000146913'  , 'encounter_id' : '6d2c0112-dcbd-447c-973e-7b756b2bf137' }  ,
            { 'health_id' : '98000120785'  , 'encounter_id' : '92e6a8bd-193b-4d55-b2af-e41a12e392df' }  ,
            { 'health_id' : '98000123649'  , 'encounter_id' : 'fe2fcbe1-92d5-4cdb-ae40-d5e59fffa7c0' }  ,
            { 'health_id' : '98000104425'  , 'encounter_id' : 'eacaa730-ccf4-4e19-a06b-d7ee3eeea5b2' }  ,
            { 'health_id' : '98000113368'  , 'encounter_id' : '688f8b7e-0c42-4c82-9117-d70184488b74' }  ,
            { 'health_id' : '98000139421'  , 'encounter_id' : 'd69899c5-6682-4370-81a1-1eac36d6e200' }  ,
            { 'health_id' : '98000179989'  , 'encounter_id' : '8fcdd271-d5cf-41bd-b88a-29e4e874aa8a' }  ,
            { 'health_id' : '98000178999'  , 'encounter_id' : 'e646867e-7739-4515-b179-247d75be5cc3' }  ,
            { 'health_id' : '98000113947'  , 'encounter_id' : '33ffb014-d29f-4143-a0c6-a08338dccffc' }  ,
            { 'health_id' : '98000152903'  , 'encounter_id' : '6de007b4-4e5a-4497-ad6d-e9ab767334e2' }  ,
            { 'health_id' : '98000151988'  , 'encounter_id' : 'a2d6b6e3-6b9d-49ed-bab4-7f830444aa88' }  ,
            { 'health_id' : '98000104557'  , 'encounter_id' : 'f85a009b-174a-4571-a623-10bbd6b889d9' }  ,
            { 'health_id' : '98000167984'  , 'encounter_id' : '69ce0b2b-2c88-4b90-b885-4faa5efea57a' }  ,
            { 'health_id' : '98000109804'  , 'encounter_id' : '5fa1b5ac-6ace-4fb5-90d4-ce0d30d544f4' }  ,
            { 'health_id' : '98000117922'  , 'encounter_id' : 'e7f9e359-c906-469a-8457-d97356e74d90' }  ,
            { 'health_id' : '98000180813'  , 'encounter_id' : '8994ddd4-e365-44bf-9025-886c9c7ec60e' }  ,
            { 'health_id' : '98000174188'  , 'encounter_id' : '31b3d8d7-7582-48db-912e-23a3959e9eb4' }  ,
            { 'health_id' : '98000119928'  , 'encounter_id' : '43cdd4c9-7341-4d4c-ba22-efef54aa4f1a' }  ,
            { 'health_id' : '98000105299'  , 'encounter_id' : 'd7b96f98-9070-4d97-a8f0-e2b5524b606a' }  ,
            { 'health_id' : '98000150303'  , 'encounter_id' : 'bed004ea-11ff-4240-aa17-666c11052a01' }  ,
            { 'health_id' : '98000173685'  , 'encounter_id' : '294d7779-b6ca-4c67-b3ce-26b338ab9e42' } ];
        var encounterRequest = null;
        var encounterRequestToBeUpdated = null;
        async.each(result, function (encounter_info, callback) {
            encounterRequest = new EncounterRequest(encounter_info.health_id, user);
            request(encounterRequest.get(encounter_info.encounter_id), function (err, res, body) {
                if (JSON.parse(body).content != null && JSON.parse(body).content.indexOf("Immunization") > 0) {
                    encounterRequestToBeUpdated = new EncounterRequest(encounter_info.health_id, facility_user, {details: cleanedUpEncounterData(JSON.parse(body).content, updation_list)});

                    request(encounterRequestToBeUpdated.put(encounter_info.encounter_id), function (err, res, body) {
                        util.log("------------------------------------------------------------------------------------------------");
                        util.log(body + "\n" + encounter_info.health_id + "  " + encounter_info.encounter_id);
                        util.log("------------------------------------------------------------------------------------------------");
                        callback();
                    });
                }
                else {
                    callback();
                }
            });
        });
    };

    async.waterfall([
        function getAccessTokenForFacilityUser(next) {
            request(new SSORequest(facility_user).post(), function (err, res, body) {
                facility_user.access_token = JSON.parse(res.body).access_token;
                next();
            });

        },
        function getAccessTokenForDatasenseUser(next) {
            request(new SSORequest(user).postBy(facility_user), function (err, res, body) {
                user.access_token = JSON.parse(res.body).access_token;
                next();
            });
        },
        function getPatientList(next) {
            new CassandraClient('172.18.46.57', 'freeshr', 'cassandra', 'cassandra').executeQuery("select encounter_id, health_id from encounter", updateEncounter);

        }
    ], function () {

    });
}

EncounterUpdater();



