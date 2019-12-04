
var mongo = require("mongodb").MongoClient;
var assert = require("assert");

const url = "mongodb://18.231.198.85:27017";
const dbName = "dashboard";
var connMongoDB = function(dados,res) {
mongo.connect(url, function(err, client) {
assert.equal(null, err);
console.log("Connected successfully to server");
const db = client.db(dbName);
return db;
query(db, dados);

client.close();

});
};
function query(db, dados) {
var collection = db.collection(dados.collection);
switch (dados.operacao) {
case "inserir":
collection.insertOne(dados.usuario, dados.callback);
break;
case "autenticar":
	
	var teste = collection.find({username: {$eq: dados.usuario}, password:{$eq: dados.password} }).toArray(function(err,result){
		
		return result;
		
		
	});

	break;
default:
break;
}
}
module.exports = function() {
return connMongoDB;
}