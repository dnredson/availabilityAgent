var Client = require('node-rest-client').Client;
 
var client = new Client();
var args = {
    headers: { "fiware-service": "openiot", "fiware-servicepath": "/"}

}
// direct way


client.get("http://177.104.61.52:1026/v2/entities/urn:ngsd-ld:Matopiba:Cloud:Services",args, function (data, response) {
    // parsed response body as js object
    
   // console.log(data.attribute.value)
    // raw response
    
});


//client.registerMethod("getServices", "http://177.104.61.52:1026/v2/entities/urn:ngsd-ld:Matopiba:Cloud:Services", "GET");
client.registerMethod("getServices", "http://177.104.61.52:1026/v2/entities/urn:ngsd-ld:Teste", "GET");
client.methods.getServices(args,function (data, response) {
   

  numberOfServices= Object.keys( data.attribute.value ).length;
  for (i=0;i <numberOfServices; i++){
   // console.log(data.attribute.value[i].name);
    startService(data.attribute.value[i].name,data.attribute.value[i].port);


  }
  
  
    // for(i=0;i<data.attribute.value.lenght)
    


});


function startService(service,port){
    var ping = require('ping');
    
   
        ping.sys.probe(service, function(isAlive){
        var msg = isAlive ? 'host ' + service + ' is alive' : 'host ' + service + ' is dead';
        console.log(msg,port);
   
    });

    
        
}