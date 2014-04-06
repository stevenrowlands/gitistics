'use strict';

var gitistics = angular.module('gitistics');

gitistics.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/repositories', {
		templateUrl : 'pages/repositories.html',
		controller : 'RepositoriesController'
	}).when('/repository/:repositoryName/byYear', {
		templateUrl : 'pages/byYear.html',
		controller : 'ByYearController'
	}).when('/repository/:repositoryName/byMonth', {
		templateUrl : 'pages/byMonth.html',
		controller : 'ByMonthController'
	}).otherwise({
		redirectTo : '/repositories'
	});
} ]);
