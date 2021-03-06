'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AuthorsController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
		groups : ["AUTHOR"],
		orders : [{order: "COMMITS", direction: "DESC"}],
		parentCount: 1,
		pageSize: -1
    }
	
	$scope.repository = $routeParams.repositoryName;
		
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});