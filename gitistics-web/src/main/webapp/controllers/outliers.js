'use strict';

var gitistics = angular.module('gitistics');

gitistics.controller('OutliersController', function($scope, $modal, $routeParams, $http) {
    
	$scope.pageNumber = 0;
	$http.get('rest/outliers/list').success(function(data) {
		$scope.outliers = data;
	});
	
	$scope.go = function(outlier) {
		outlier.valid = !outlier.valid;
		$http.post('rest/outliers/toggleValid', outlier).success(function(data) {
			
		});
	}
	$scope.apply = function(filter) {
		$scope.pageNumber = 0;
		var config = {
	    	params: { dateFrom: filter.dateFrom, dateTo : filter.dateTo, repositoryName: $routeParams.repositoryName, authorName: filter.authorName}
	    }
		$http.get('rest/outliers/list', config).success(function(data) {
			$scope.outliers = data;
		});
	}
	
	$scope.page = function(filter) {
		var config = {
		    params: { page : $scope.pageNumber + 1, dateFrom: filter.dateFrom, dateTo : filter.dateTo, repositoryName: $routeParams.repositoryName, authorName: filter.authorName }
		}
		$scope.pageNumber = $scope.pageNumber + 1;
		$http.get('rest/outliers/list', config).success(function(data) {
			for (var d in data) {
				$scope.outliers.push(data[d]);
			}
		});
	}

});