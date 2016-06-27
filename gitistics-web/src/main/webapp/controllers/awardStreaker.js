'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AwardStreakerController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
    }
	
	$scope.repository = $routeParams.repositoryName;
		
	$http.post('rest/random/streaker', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});