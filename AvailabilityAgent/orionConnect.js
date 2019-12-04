var http = require('http');
var fs = require("fs");
var monitoring = require('./monitoring');

var jsonData = JSON.parse(fs.readFileSync("manager.conf", "utf8"));

var opcoes = {
	hostname: jsonData.brokerAddress,
	port: jsonData.brokerPort,
	path: '/v2/entities/'+jsonData.monitoringEntity,
	hostname: '177.104.61.60',
	port: 1026,
    json: true,
	headers: {
		'Accept' : 'application/json',
		'fiware-service':'openiot',
		'fiware-service-path':'/'
	}
}

var response = [];
var jsonResponse ={};
var jsonResponse2;
var req = http.request(opcoes,function(res){

    res.on('data', function(pedaco){
        response.push(pedaco);
       
        });
        res.on('end',function(res){
			jsonResponse = JSON.parse(response);
			
			monitoring.start(jsonResponse);
			
          
        });

});
console.log("JSON");

req.end();

