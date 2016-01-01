"use strict";
function writeResults(result){
    //var index = 0;
    //var health_ids = [];
    //var uniq_health_ids = []
    //for(index = 0; index < result.length; index++){
    //    health_ids.push(result[index].health_id);
    //}
    //uniq_health_ids = require("uniq")(health_ids);
    //console.log(uniq_health_ids.sort());

    console.log(result);
}
var CassandraClient = require('./../misc/cassandra_client').CassandraClient;
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101001157'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101021767'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101540337'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101754730'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101763764'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101888090'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98101896309'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98102245654'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98102262881'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98102581744'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98102654772'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98102688770'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98103055102'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98103113174'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98103218684'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104105054'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104161339'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104211076'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104433688'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104597573'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104823235'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98104930337'", writeResults);
new CassandraClient('172.18.46.56','mci','cassandra','cassandra').executeQuery("select *from patient_duplicate where health_id2= '98105031556'", writeResults);

