'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AwardAuthorController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
	    groups : ["YEAR", "FILE_TYPE", "AUTHOR"],
	    orders : [{order: "YEAR", direction: "DESC"}, {order: "FILE_TYPE", direction: "ASC"} , {order: "COMMITS", direction: "DESC"}],
	 	pageSize : -1
    }
	
	$scope.repository = $routeParams.repositoryName;
		
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});