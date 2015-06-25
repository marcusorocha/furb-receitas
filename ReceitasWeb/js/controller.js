'use strict';

var appControllers = angular.module('appControllers', []);


var receitaCtrlImpl = function($scope, $routeParams, $rootScope, ReceitaDAO, DadosDAO)
{
	$scope.usuarioLogado = function() 
	{ 
		if ($rootScope.globals)
		{
			$scope.usuario = $rootScope.globals.currentUser;
		}	
		
		if ($scope.usuario)
			return true;
		else
			return false;
	}

	$scope.loadAll = function()
	{	
		var filtro = $routeParams.busca;
		
		if (filtro)
		{
			$scope.buscarReceitaPorDescricao(filtro);
		}
		else
		{
			$scope.receitas = ReceitaDAO.query();
		}
	};
	
	$scope.salvar = function()
	{	
		ReceitaDAO.save($scope.novareceita, function(receita)
		{								
			if ($scope.novareceita.oid)
			{
				for(var i in $scope.especiarias)
		        {
		            if ($scope.receitas[i].oid == $scope.novareceita.oid)
		            {
		                $scope.receitas[i] = angular.copy($scope.novareceita);
		            }
		        }				
			}
			else
			{
				$scope.receitas.push(angular.copy(receita));	
			}
	        $scope.novareceita = {};
		});
	};
	
	$scope.excluir = function(oid)
	{
		ReceitaDAO.remove({ oid: oid }, function()
		{			
	        for(var i in $scope.receitas)
	        {
	            if($scope.receitas[i].oid == oid)
	            {
	                $scope.receitas.splice(i,1);
	            }
	        }
		});
	};
	
	$scope.editar = function(oid)
	{
        for(var i in $scope.receitas)
        {
            if ($scope.receitas[i].oid == oid)
            {
                $scope.novareceita = angular.copy($scope.receitas[i]);
            }
        }
    }
    
    $scope.find = function(receita)
	{
		ReceitaDAO.get({oid:receita.oid}, function(Receita)
		{
			$scope.nome = Especiaria.nome;
		});
	};
	
	$scope.buscarReceitaPorDescricao = function(descricao)
	{	
		DadosDAO.buscaReceitasPorDescricao(descricao)
	        .success(function (receitas)
	        {
	            $scope.receitas = receitas;
	        });
	}
	
	$scope.detalhar = function()
	{
		var id_receita = $routeParams.id;
	
		ReceitaDAO.get({oid:id_receita}, function(receita)
		{
			$scope.receita = angular.copy(receita);
		});
	
		DadosDAO.buscaIngredientesReceita(id_receita)
	        .success(function (ingredientes)
	        {
	            //$scope.status = 'Retrieved orders!';
	            $scope.ingredientes = ingredientes;
	        })
	        .error(function (error)
	        {
	            //$scope.status = 'Error retrieving customers! ' + error.message;
	        });
	        
	    DadosDAO.buscaPassosReceita(id_receita)
	        .success(function (passos)
	        {
	            //$scope.status = 'Retrieved orders!';
	            $scope.passos = passos;
	        })
	        .error(function (error)
	        {
	            //$scope.status = 'Error retrieving customers! ' + error.message;
	        });
	}
};

var ingredientesCtrlImpl = function($scope, $routeParams, IngredienteDAO, DadosDAO)
{	
	$scope.loadFromReceita = function()
	{	
		DadosDAO.buscaIngredientesReceita({oid:$scope.receita.oid})
	        .success(function (ingredientes)
	        {
	            //$scope.status = 'Retrieved orders!';
	            $scope.ingredientes = ingredientes;
	        })
	        .error(function (error)
	        {
	            //$scope.status = 'Error retrieving customers! ' + error.message;
	        });
	}
	
	$scope.salvar = function()
	{	
		IngredienteDAO.save($scope.novoingrediente, function(ingrediente)
		{								
			if ($scope.novoingrediente.oid)
			{
				for(var i in $scope.ingredientes)
		        {
		            if ($scope.ingredientes[i].oid == $scope.novoingrediente.oid)
		            {
		                $scope.ingredientes[i] = angular.copy($scope.novoingrediente);
		            }
		        }				
			}
			else
			{
				$scope.ingredientes.push(angular.copy(ingrediente));
			}
	        $scope.novoingrediente = {};
		});
	};
	
	$scope.excluir = function(oid)
	{
		IngredienteDAO.remove({ oid: oid }, function()
		{			
	        for(var i in $scope.ingredientes)
	        {
	            if($scope.ingredientes[i].oid == oid)
	            {
	                $scope.ingredientes.splice(i,1);
	            }
	        }
		});
	};
	
	$scope.editar = function(oid)
	{
        for(var i in $scope.ingredientes)
        {
            if ($scope.ingredientes[i].oid == oid)
            {
                $scope.novoingrediente = angular.copy($scope.ingredientes[i]);
            }
        }
    }
};

var loginCtrlImpl = function ($scope, $rootScope, $location, AuthenticationService) 
{
		
	$scope.showlogin = function()
	{
		if ($rootScope.globals)
		{
			$scope.usuario = $rootScope.globals.currentUser;
		}
		
		if ($scope.usuario)
			return false;
		else
			return true;
	}
    

    $scope.login = function ()
    {
        $scope.processando = true;
        
        AuthenticationService.Login($scope.username, $scope.password).success(function(response)
        {
            if (response.token)
            {
                AuthenticationService.SetCredentials($scope.username, response.token);                
            } 
            else 
            {
                $scope.error = "Usuário ou senha inválidos";                
                $scope.alert($scope.error);
            }
            
            $scope.processando = false;
        });
    };
    
    $scope.logout = function()
	{
		$scope.processando = true;
		
		AuthenticationService.ClearCredentials();
		
		$scope.processando = false;
		
		$location.path('/');
	}
};

var homeCtrlImpl = function ($scope, $routeParams, $rootScope, $location)
{	
	$scope.goTo = function(location)
	{
		$location.path('/' + location);
	}
	
	$scope.buscar = function()
	{
		//$routeParams.busca = $scope.receitas.filtro;
		
		//$rootScope.globals.receitas.busca = $scope.receitas.filtro;
		
		//$location.path('receitas?busca=' + $scope.receitas.filtro);
		
		$location.path('/receitas').search('busca', $scope.receitas.filtro);
	}
};

var novaReceitaCtrlImpl = function($scope, $routeParams, $rootScope, ReceitaDAO, IngredienteDAO, PassoDAO, DadosDAO)
{
	$scope.salvarReceita = function()
	{	
		//ReceitaDAO.save($scope.novareceita, function(receita)
		DadosDAO.salvarReceita($scope.novareceita.descricao).success(function(receita)
		{								
			$scope.novareceita = angular.copy(receita);
		});
	};

	$scope.salvarIngrediente = function()
	{	
		IngredienteDAO.save($scope.novoingrediente, function(ingrediente)
		{								
			if ($scope.novoingrediente.oid)
			{
				for(var i in $scope.ingredientes)
		        {
		            if ($scope.ingredientes[i].oid == $scope.novoingrediente.oid)
		            {
		                $scope.ingredientes[i] = angular.copy($scope.novoingrediente);
		            }
		        }				
			}
			else
			{
				$scope.ingredientes.push(angular.copy(ingrediente));
			}
	        $scope.novoingrediente = {};
		});
	};	
	
	$scope.salvarPasso = function()
	{	
		PassoDAO.save($scope.novopasso, function(passo)
		{								
			if ($scope.novopasso.oid)
			{
				for(var i in $scope.passos)
		        {
		            if ($scope.passos[i].oid == $scope.novopasso.oid)
		            {
		                $scope.passos[i] = angular.copy($scope.novopasso);
		            }
		        }				
			}
			else
			{
				$scope.passos.push(angular.copy(passo));
			}
	        $scope.novopasso = {};
		});
	};

}

/* Registro dos Controladores */
appControllers.controller('ReceitaController', ['$scope', '$routeParams', '$rootScope', 'ReceitaDAO', 'DadosDAO', receitaCtrlImpl]);
appControllers.controller('NovaReceitaController', ['$scope', '$routeParams', '$rootScope', 'ReceitaDAO', 'IngredienteDAO', 'PassoDAO', 'DadosDAO', novaReceitaCtrlImpl]);
appControllers.controller('IngredientesController', ['$scope', '$routeParams', 'IngredientesDAO', 'DadosDAO', ingredientesCtrlImpl]);
appControllers.controller('LoginController', ['$scope', '$rootScope', '$location', 'AuthenticationService', loginCtrlImpl]);
appControllers.controller('HomeController', ['$scope', '$routeParams', '$rootScope', '$location', homeCtrlImpl]);