module.exports = function(application){
	application.get('/', function(req, res){
		application.app.controllers.index.index(application, req, res);
	});

	application.post('/auth', function(req, res){
		application.app.controllers.index.auth(application, req, res);
	});

	application.post('/login', function(req, res){
		application.app.controllers.index.login(application, req, res);
	
	});
}

