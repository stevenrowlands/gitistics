'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AwardJanitorController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
	    groups : ["YEAR", "AUTHOR"],
	    message : "clean",
	    orders : [{order: "YEAR", direction: "DESC"}, {order: "COMMITS", direction: "DESC"}],
	 	pageSize : -1
    }
	
	$scope.repository = $routeParams.repositoryName;
		
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});