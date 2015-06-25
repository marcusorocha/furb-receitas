'use strict';

var app = angular.module('receitasApp', ['ngRoute', 'ngCookies', 'appControllers','appServices']);

var roteamentos = function ($routeProvider) 
{
	$routeProvider.
	when('/home',	{ templateUrl: 'views/Home.html', controller: 'HomeController' }).
	when('/receitas', { templateUrl: 'views/Receitas.html', controller: 'ReceitaController' }).
	when('/receitas/:id/detalhes', { templateUrl: 'views/ReceitasDetalhes.html', controller: 'ReceitaController' }).
    when('/receitas/nova', {templateUrl: 'views/IncluirReceita.html', controller: 'NovaReceitaController'}).
    when('/login', {templateUrl: 'views/LoginForm.html', controller: 'LoginController'}).
	otherwise({ redirectTo: '/home' });
};

var inicializacao = function ($rootScope, $location, $cookieStore, $http) 
{
    // keep user logged in after page refresh
    $rootScope.globals = $cookieStore.get('globals') || {};
    
    if ($rootScope.globals.currentUser) 
    {
        $http.defaults.headers.common['Authorization'] = $rootScope.globals.currentUser.authdata;
    }

    $rootScope.$on('$locationChangeStart', function (event, next, current) 
    {
        // redirect to login page if not logged in
        //if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
        //   $location.path('/login');
        //}
    });
};

app.config(['$routeProvider', roteamentos]).run(['$rootScope', '$location', '$cookieStore', '$http', inicializacao]);

