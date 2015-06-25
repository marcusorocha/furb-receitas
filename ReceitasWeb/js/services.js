'use strict';

/* Services */

var host = "http://receitas-rs.elasticbeanstalk.com";
//var host = "http://localhost:8080/receitas-rs";

var services = angular.module('appServices', ['ngResource']);

/* Implementação dos Serviços */

var receitaServiceImpl = function ($resource)
{	
	var url = host + "/rs/receita/:oid";
	
	var acoes = {
					'get':    {method:'GET'},
					'save':   {method:'POST'},
					'query':  {method:'GET', isArray:true},
					'remove': {method:'DELETE'},
				};
	
	return $resource(url, acoes);
};

var ingredienteServiceImpl = function ($resource)
{	
	var url = host + "/rs/ingrediente/:oid";
	
	var acoes = {
					'get':    {method:'GET'},
					'save':   {method:'POST'},
					'remove': {method:'DELETE'},
				};
	
	return $resource(url, acoes);
};

var passoServiceImpl = function ($resource)
{	
	var url = host + "/rs/passo/:oid";
	
	var acoes = {
					'get':    {method:'GET'},
					'save':   {method:'POST'},
					'remove': {method:'DELETE'},
				};
	
	return $resource(url, acoes);
};

var dadosServiceImpl = function ($http)
{
    var url = host + "/rs";
    
    var DAO = {};

    DAO.buscaReceitasPorDescricao = function (descricao)
    {
        return $http.post(url + '/receita/descricao/', "descricao=" + encodeURIComponent(descricao));
    };
    
    DAO.buscaIngredientesReceita = function (id_receita)
    {
	    return $http.get(url + '/ingrediente/receita/' + id_receita);
    }
    
    DAO.buscaPassosReceita = function (id_receita)
    {
	    return $http.get(url + '/passo/receita/' + id_receita);
    }
    
    DAO.salvarReceita = function(descricao)
    {
	    return $http.post(url + '/receita', "descricao=" + encodeURIComponent(descricao));
    }
    
    return DAO;
}

var authServiceImpl = function ($http, $rootScope, $cookieStore)
{
    var service = {};

    service.Login = function (usuario, senha)
    {       
    	var url = host + "/rs/usuarios/login";
    	
        return $http.post(url, "nome=" + encodeURIComponent(usuario) + "&senha=" + encodeURIComponent(senha));
    };

    service.SetCredentials = function (usuario, token)
    {
        //var authdata = Base64.encode(username + ':' + password);
        var authdata = token;

        $rootScope.globals = {
            currentUser: {
                username: usuario,
                authdata: authdata
            }
        };

        $http.defaults.headers.common['Authorization'] = authdata;
        $cookieStore.put('globals', $rootScope.globals);
    };

    service.ClearCredentials = function () 
    {
        $rootScope.globals = {};
        $cookieStore.remove('globals');
        $http.defaults.headers.common.Authorization = '';
    };

    return service;
};

/* Registro dos Serviços */
services.factory('ReceitaDAO', ['$resource', receitaServiceImpl]);
services.factory('IngredienteDAO', ['$resource', ingredienteServiceImpl]);
services.factory('PassoDAO', ['$resource', passoServiceImpl]);
services.factory('DadosDAO', ['$http', dadosServiceImpl]);
services.factory('AuthenticationService', ['$http', '$rootScope', '$cookieStore', authServiceImpl]);