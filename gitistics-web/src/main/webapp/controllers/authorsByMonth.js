'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AuthorsByMonthController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
		groups : ["YEAR", "MONTH", "AUTHOR"],
		orders : [{order: "YEAR", direction: "DESC"}, {order: "MONTH", direction: "DESC"}, {order: "COMMITS", direction: "DESC"}],
		pageSize : -1
	}
		
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});