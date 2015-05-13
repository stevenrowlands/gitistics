'use strict';

var gitistics = angular.module('gitistics');

gitistics.controller('FileTypeByYearController', function($scope, $modal, $routeParams, $http) {
	
	$scope.repository = $routeParams.repositoryName;
	
	var params = { 
			repositoryName : $routeParams.repositoryName,
		    groups : ["YEAR", "FILE_TYPE"],
		    orders : [{order: "YEAR", direction: "DESC"}, {order: "COMMITS", direction: "DESC"}],
		 	pageSize : -1
	    }
			
	$http.post('rest/statistics/statistics', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);

});


