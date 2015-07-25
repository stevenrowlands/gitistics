'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('TeamTesterByMonthController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
	    groups : ["YEAR", "MONTH"],
	    fileName : "%Test%",
	    orders : [{order: "YEAR", direction: "DESC"},{order: "MONTH", direction: "DESC"}, {order: "COMMITS", direction: "DESC"}],
	 	pageSize : -1
    }
	
	$scope.repository = $routeParams.repositoryName;
		
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});