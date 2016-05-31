'use strict';

var gitistics = angular.module('gitistics');

gitistics.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/repositories', {
		templateUrl : 'pages/repositories.html',
		controller : 'RepositoriesController'
	}).when('/repository/:repositoryName/byYear', {
		templateUrl : 'pages/byYear.html',
		controller : 'ByYearController'
	}).when('/repository/:repositoryName/summary', {
		templateUrl : 'pages/summary.html',
		controller : 'SummaryController'
	}).when('/repository/:repositoryName/byMonth', {
		templateUrl : 'pages/byMonth.html',
		controller : 'ByMonthController'
	}).when('/repository/:repositoryName/outliers', {
		templateUrl : 'pages/outliers.html',
		controller : 'OutliersController'
	}).when('/repository/:repositoryName/authors', {
		templateUrl : 'pages/authors.html',
		controller : 'AuthorsController'
	}).when('/repository/:repositoryName/authorsByMonth', {
		templateUrl : 'pages/authorsByMonth.html',
		controller : 'AuthorsByMonthController'
	}).when('/repository/:repositoryName/authorsByYear', {
		templateUrl : 'pages/authorsByYear.html',
		controller : 'AuthorsByYearController'
	}).when('/repository/:repositoryName/fileType', {
		templateUrl : 'pages/fileType.html',
		controller : 'FileTypeController'
	}).when('/repository/:repositoryName/fileTypeByYear', {
		templateUrl : 'pages/fileTypeByYear.html',
		controller : 'FileTypeByYearController'
	}).when('/repository/:repositoryName/awardReverter', {
		templateUrl : 'pages/awardReverter.html',
		controller : 'AwardReverterController'
	}).when('/repository/:repositoryName/awardTester', {
		templateUrl : 'pages/awardTester.html',
		controller : 'AwardTesterController'
	}).when('/repository/:repositoryName/awardJanitor', {
		templateUrl : 'pages/awardJanitor.html',
		controller : 'AwardJanitorController'
	}).when('/repository/:repositoryName/awardAuthor', {
		templateUrl : 'pages/awardAuthor.html',
		controller : 'AwardAuthorController'
	}).when('/repository/:repositoryName/teamTester', {
		templateUrl : 'pages/teamTester.html',
		controller : 'TeamTesterController'
	}).when('/repository/:repositoryName/teamTesterByMonth', {
		templateUrl : 'pages/teamTesterByMonth.html',
		controller : 'TeamTesterByMonthController'
	}).when('/repository/:repositoryName/defects', {
		templateUrl : 'pages/defects.html',
		controller : 'DefectsController'
	}).otherwise({
		redirectTo : '/repositories'
	});
} ]);
