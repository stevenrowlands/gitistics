'use strict';

var gitistics = angular.module('gitistics');

gitistics.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/repositories', {
		templateUrl : 'pages/repositories.html',
		controller : 'RepositoriesController'
	}).otherwise({
		redirectTo : '/repositories'
	});
} ]);
