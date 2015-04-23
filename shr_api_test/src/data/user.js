module.exports = function (user_type) {

    var environment = process.env.ENVIRONMENT || 'QA';
    var user = {};
    user["QA"] = {};
    user["SHOWCASE"] = {};
    user["STAGING"] = {};

    user["QA"]["datasense"] = {
        email: "rappasam@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18552,
        api_token: 'b7aa1f4001ac4b922dabd6a02a0dabc44cf5af74a0d1b68003ce7ccdb897a1d2',
        access_token: "",
        catchment: "3026"
    };

    user["QA"]["facility"] = {
        email: "dmishra@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18549,
        api_token: '1c2a599423203f639dcdd8574ac5391dd67d21316ea30ee364c8a8787fb79dd3',
        access_token: "",
        catchment: ["302607"]
    };

    user["QA"]["mciAdmin"] = {
        email: "ashutoks@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18557,
        api_token: null,
        access_token: ""
    };
    user["QA"]["mci_approver"] = {
        email: "ranjan@thoughtworks.com",
        password: "thoughtworks",
            client_id: 18559,
        api_token: null,
        access_token: "",
        catchment: ["3026"]
    };


    user["QA"]["provider"] = {
        email: "monikar@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18556,
        api_token: 'af6d37106aa4d5fbda63c9c29b264dc4e3de6e35362fcc997659a18a58ce42ac',
        access_token: "",
        catchment: ["302607"]
    };

    user["QA"]["confidential_patient"] = {
        email: "utsabban@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18558,
        api_token: null,
        access_token: "",
        hid: '11569149358'

    };

    user["QA"]["patient"] = {
        email: "hitansuj@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18560,
        api_token: null,
        access_token: "",
        hid: '11569128243'

    };
    user["QA"]["shr"] = {
        email: "mritunjd@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18550,
        api_token: 'c6e6fd3a26313eb250e1019519af33e743808f5bb50428ae5423b8ee278e6fa5',
        access_token: "",
        catchment: ["302607"]


    };


    user["SHOWCASE"]["datasense"] = {
        email : "datasense@test.com",
        password : "thoughtworks",
        client_id : 18565,
        api_token : '322d16010e1fb5728f34fbb9bd8567184e7220472e58220c5814ca9f63c01795',
        access_token : "",
        catchment : "3026"
    };

    user["SHOWCASE"]["facility"] = {
        email : "facility@test.com",
        password : "thoughtworks",
        client_id : 18563,
        api_token : '697a61a6527565d1c98d7b9837ae1f25a6da9884d3fa7f75f66c93e4740e89bf',
        access_token : "",
        catchment : ["302607"]
    };

    user["SHOWCASE"]["mciAdmin"] = {
        email : "MciAdmin@test.com",
        password : "thoughtworks",
        client_id : 18564,
        api_token : null,
        access_token : "",
        catchment : ["3026"]
    };
    user["SHOWCASE"]["mci_approver"] = {
        email : "mciApprover@test.com",
        password : "thoughtworks",
        client_id : 18566,
        api_token : null,
        access_token : "",
        catchment : ["3026"]
    };


    user["SHOWCASE"]["provider"] = {
        email : "provider@test.com",
        password : "thoughtworks",
        client_id : 18562,
        api_token : 'bec81dddaa8a400a51e10710f7a82ef3f04cfddaa9bbe5ac051328c46a6373fd',
        access_token : "",
        catchment : ["302607"]
    };

    user["SHOWCASE"]["confidential_patient"] = {
        email : "confendial@test.com",
        password : "thoughtworks",
        client_id : 18570,
        api_token : null,
        access_token : "",
        hid : '11448462025'

    };

    user["SHOWCASE"]["patient"] = {
        email : "patient@test.com",
        password : "thoughtworks",
        client_id : 18568,
        api_token : null,
        access_token : "",
        hid : '11448451811'

    };
    user["SHOWCASE"]["shr"] = {
        email : "shr@test.com",
        password : "thoughtworks",
        client_id : 18567,
        api_token : 'aa0506c979afeccbaaff70bcb6cb88bca7b0371252fdb8d7192ab1db851e59ad',
        access_token : "",
        catchment : ["302607"]


    };

    user["SHOWCASE"]["PatientJournal"] = {
        email : "patientJournal@test.com",
        password : "thoughtworks",
        client_id : 18569,
        api_token : '',
        access_token : ""



    };


    user["STAGING"]["datasense"] = {
        email : "datasense@test.com",
        password : "thoughtworks",
        client_id : 18565,
        api_token : '322d16010e1fb5728f34fbb9bd8567184e7220472e58220c5814ca9f63c01795',
        access_token : "",
        catchment : "3026"
    };

    user["STAGING"]["facility"] = {
        email : "facility@test.com",
        password : "thoughtworks",
        client_id : 18563,
        api_token : '697a61a6527565d1c98d7b9837ae1f25a6da9884d3fa7f75f66c93e4740e89bf',
        access_token : "",
        catchment : ["302607"]
    };

    user["STAGING"]["mciAdmin"] = {
        email : "MciAdmin@test.com",
        password : "thoughtworks",
        client_id : 18564,
        api_token : null,
        access_token : "",
        catchment : ["3026"]
    };
    user["STAGING"]["mci_approver"] = {
        email : "mciApprover@test.com",
        password : "thoughtworks",
        client_id : 18566,
        api_token : null,
        access_token : "",
        catchment : ["3026"]
    };


    user["STAGING"]["provider"] = {
        email : "provider@test.com",
        password : "thoughtworks",
        client_id : 18562,
        api_token : 'bec81dddaa8a400a51e10710f7a82ef3f04cfddaa9bbe5ac051328c46a6373fd',
        access_token : "",
        catchment : ["302618"]
    };

    user["STAGING"]["confidential_patient"] = {
        email : "confendialStage@test.com",
        password : "thoughtworks",
        client_id : 18571,
        api_token : null,
        access_token : "",
        hid : '11613787492'

    };

    user["STAGING"]["patient"] = {
        email : "patientStage@test.com",
        password : "thoughtworks",
        client_id : 18572,
        api_token : null,
        access_token : "",
        hid : '11613787672'

    };
    user["STAGING"]["shr"] = {
        email : "shr@test.com",
        password : "thoughtworks",
        client_id : 18567,
        api_token : 'aa0506c979afeccbaaff70bcb6cb88bca7b0371252fdb8d7192ab1db851e59ad',
        access_token : "",
        catchment : ["302607"]


    };

    user["STAGING"]["PatientJournal"] = {
        email : "patientJournal@test.com",
        password : "thoughtworks",
        client_id : 18569,
        api_token : '',
        access_token : ""



    };


    return user[environment][user_type];


};
