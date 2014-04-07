'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AuthorsByYearController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
	    groups : ["YEAR", "AUTHOR"],
	    orders : [{order: "YEAR", direction: "DESC"}, {order: "COMMITS", direction: "DESC"}],
	 	pageSize : -1
    }
		
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});