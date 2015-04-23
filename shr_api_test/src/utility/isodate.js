"use strict";

exports.isodate = function ()
{
    var pad  = function (n) {
        return n < 10 ? '0' + n : n;
    };

    var isoDate = function () {
        var d = new Date();
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + 'T' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds()) + "+05:30";

    };

    var isoDateEncoded = function () {
        var d = new Date();
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + 'T' + pad(d.getHours()) + '%3A' + pad(d.getMinutes()) + '%3A' + pad(d.getSeconds()) + "%2B05%3A30";

    };

    return {
        isoDate : isoDate,
        isoDateEncoded : isoDateEncoded
    };

};



