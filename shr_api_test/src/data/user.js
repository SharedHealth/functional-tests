module.exports = function (user_type) {

    var environment = process.env.ENVIRONMENT || 'QA';
    var user = {};
    user["QA"] = {};
    user["SHOWCASE"] = {};

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
        hid: '11214394110'

    };

    user["QA"]["patient"] = {
        email: "hitansuj@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18560,
        api_token: null,
        access_token: "",
        hid: '11246363561'

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
        email: "rappasam@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18552,
        api_token: 'b7aa1f4001ac4b922dabd6a02a0dabc44cf5af74a0d1b68003ce7ccdb897a1d2',
        access_token: "",
        catchment: "3026"
    };

    user["SHOWCASE"]["facility"] = {
        email: "dmishra@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18549,
        api_token: '1c2a599423203f639dcdd8574ac5391dd67d21316ea30ee364c8a8787fb79dd3',
        access_token: "",
        catchment: ["302607"]
    };

    user["SHOWCASE"]["mciAdmin"] = {
        email: "imran@rightbrainsolution.com",
        password: "rightbrain",
        client_id: 18555,
        api_token: null,
        access_token: "",
        catchment: ["3026"]

    };
    user["SHOWCASE"]["mci_approver"] = {
        email: "imran@rightbrainsolution.com",
        password: "rightbrain",
        client_id: 18555,
        api_token: null,
        access_token: "",
        catchment: ["3026"]
    };


    user["SHOWCASE"]["provider"] = {
        email: "monikar@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18556,
        api_token: 'af6d37106aa4d5fbda63c9c29b264dc4e3de6e35362fcc997659a18a58ce42ac',
        access_token: "",
        catchment: ["302607"]
    };

    user["SHOWCASE"]["confidential_patient"] = {
        email: "utsabban@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18558,
        api_token: null,
        access_token: "",
        hid: '11214394110'

    };

    user["SHOWCASE"]["patient"] = {
        email: "hitansuj@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18560,
        api_token: null,
        access_token: "",
        hid: '11246363561'

    };
    user["SHOWCASE"]["shr"] = {
        email: "mritunjd@thoughtworks.com",
        password: "thoughtworks",
        client_id: 18550,
        api_token: 'c6e6fd3a26313eb250e1019519af33e743808f5bb50428ae5423b8ee278e6fa5',
        access_token: "",
        catchment: ["302607"]


    };
    return user[environment][user_type];
	

};
