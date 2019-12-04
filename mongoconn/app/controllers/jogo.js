module.exports.jogo = function(application, req, res){
	var connection = application.config.dbConnection;
	var UsuariosDAO = new application.app.models.UsuariosDAO(connection);

//	UsuariosDAO.find(connection);
	console.log("deu certo?");
	
	res.render('jogo');


}