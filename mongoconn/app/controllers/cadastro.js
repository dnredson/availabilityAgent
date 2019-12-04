module.exports.cadastro = function (application, req, res){
	res.render('cadastro', {validacao: {}, dadosForm: {}});
}

module.exports.cadastrar = function(application, req, res){

	
	var dadosForm = req.body;
	

	req.assert('name', 'Nome não pode ser vazio').notEmpty();
	req.assert('username', 'Usuário não pode ser vazio').notEmpty();
	req.assert('password', 'Senha não pode ser vazio').notEmpty();
	req.assert('role', 'Casa não pode ser vazio').notEmpty();

	var erros = req.validationErrors();
console.log(erros);
	if(erros){
		res.render('cadastro', {validacao: erros, dadosForm: dadosForm});
		return;
	}

	var connection = application.config.dbConnection;
	console.log("Instancia o usuários DAO");
	var UsuariosDAO = new application.app.models.UsuariosDAO(connection);

	UsuariosDAO.inserirUsuario(dadosForm,res);

}