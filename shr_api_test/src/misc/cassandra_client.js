'use strict'
var cassandra = require('cassandra-driver');
var async = require('async');

exports.CassandraClient = function CassandraClient(contact_point, key_space, user_name, password)
{
    var client = new cassandra.Client({
        contactPoints: [contact_point],
        keyspace: key_space,
        authProvider: new cassandra.auth.PlainTextAuthProvider(user_name, password)
    });

    return {
        executeQuery : function(query,callback){
            async.series([
                function connect(next) {
                    client.connect(next);
                },

                function select(next) {
                    client.execute(query,null, {fetchSize: 20000},  function (err, result) {
                        if (err) return next(err);

                        next(null, result.rows);
                    });
                }
            ], function (err, result) {
                if (err) {
                    console.error('There was an error', err.message, err.stack);
                    result = [];
                }
                callback(result[1]);
                console.log('Shutting down');
                client.shutdown();

            });
        }

    }


};
