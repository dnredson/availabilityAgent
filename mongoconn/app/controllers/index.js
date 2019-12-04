module.exports.index = function(application, req, res){
	res.render('index', {validacao: {}});
}

module.exports.auth = function(application, req, res){
	
	var dadosForm = req.body;
	
	

	req.assert('username', 'Usuário não de ser vazio').notEmpty();
	req.assert('password', 'Senha não de ser vazia').notEmpty();


	var connection = application.config.dbConnection;
	var UsuariosDAO = new application.app.models.UsuariosDAO(connection());

	var teste = UsuariosDAO.auth(dadosForm, req, res);

	//res.send('tudo ok para criar a sessão');
}


module.exports.login = function(application, req, res){
	console.log("Body");
	console.log(res);
}