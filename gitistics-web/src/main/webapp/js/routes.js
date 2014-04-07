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
	}).when('/repository/:repositoryName/authors', {
		templateUrl : 'pages/authors.html',
		controller : 'AuthorsController'
	}).when('/repository/:repositoryName/authorsByMonth', {
		templateUrl : 'pages/authorsByMonth.html',
		controller : 'AuthorsByMonthController'
	}).when('/repository/:repositoryName/authorsByYear', {
		templateUrl : 'pages/authorsByYear.html',
		controller : 'AuthorsByYearController'
	}).otherwise({
		redirectTo : '/repositories'
	});
} ]);
