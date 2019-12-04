var http = require('http');

module.exports.start = function(entities){
    var numberOFEntities = entities.entities.value.length;
  
   for(var i= 1; i < numberOFEntities-1; i++){
    console.log(entities.entities.value[i].Destinyhost.value); 
     
       
       



   }
    

    
}