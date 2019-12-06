
async function ping(host){
var ping = require('ping');
var host = "132.141.121.1"
var resposta = await ping.promise.probe(host)
.then(function (res) {
    //console.log(res.alive);
    return res.alive;
    
});
return resposta;
}
async function available(host,port){

}

/* var alive =  ping;
alive().then(content=>{
    console.log("Tá up essa porra?");
    console.log(content);

}).catch(err=>{console.error(err);});
 */


const fetch = require("node-fetch");
const url = "http://177.104.61.17:1026/version";

const getData = async url => {
  try {
    const response = await fetch(url);
    const json = await response.json();
    return json
  } catch (error) {
    console.log(error);
  }
};

/* getData(url).then(content=>{
    console.log("Tá available essa porra?");
    console.log(content.orion.version);

}).catch(err=>{console.error(err);});
 */
  Promise.all([getData(url),ping(url)]).then(function(result1){
    console.log("result1");  
    console.log(result1);
   
  }).catch(err=>{console.log(err);})