'use strict';

var gitistics = angular.module('gitistics');

gitistics.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/repositories', {
		templateUrl : 'pages/repositories.html',
		controller : 'RepositoriesController'
	}).when('/byYear', {
		templateUrl : 'pages/byYear.html',
		controller : 'ByYearController'
	}).otherwise({
		redirectTo : '/repositories'
	});
} ]);
