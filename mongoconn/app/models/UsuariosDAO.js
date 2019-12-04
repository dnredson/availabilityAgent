var crypto = require('crypto');

function UsuariosDAO(connection) {
	this._connection = connection;
	}
	UsuariosDAO.prototype.inserirUsuario = function(usuario, res) {

	var senha = crypto.createHash("md5").update(usuario.password).digest("hex");
	
	usuario.password = senha;
	
	var dados = {
	operacao: "inserir",
	usuario: usuario,
	collection: "users",
	callback: function(err, result) {
	res.send("users");
	}
	};
	this._connection(dados);
	};


	UsuariosDAO.prototype.auth = function(usuario, req, res){
		
		var senha = crypto.createHash("md5").update(usuario.password).digest("hex");
		var dados = {
			operacao: "autenticar",
			usuario: usuario.username,
			password: senha,
			collection: "users",
			callback: function(err, result) {
				res.send("/teste");
			}
			};
			
			
				
			var collection = this._connection();
		
			var teste = collection.find({username: {$eq: dados.usuario}, password:{$eq: dados.password} }).toArray(function(err,result){
		
				console.log( result);
				
				
			});
			
	}



	module.exports = function() {
	return UsuariosDAO;
	};
	