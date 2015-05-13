'use strict';

var gitistics = angular.module('gitistics');

gitistics.controller('SummaryController', function($scope, $modal, $routeParams, $http) {

	var params = { 
		repositoryName : $routeParams.repositoryName
    }
	$scope.repository = $routeParams.repositoryName;
	
	$http.post('rest/statistics/statistics', params).success(
			function(data) {
				$scope.statistics = data;
	});
});
