'use strict';

var gitistics = angular.module('gitistics');


gitistics.controller('AwardRevertSelfController', function($scope, $modal, $routeParams, $http) {
	
	var params = { 
		repositoryName : $routeParams.repositoryName,
    }
	
	$scope.repository = $routeParams.repositoryName;
		
	$http.post('rest/random/revertself', params).success(
		function(data) {
			$scope.statistics = data;
		}
	);
});