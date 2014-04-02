'use strict';

var gitistics = angular.module('gitistics');

gitistics.controller('RepositoriesController', function($scope, $modal, $http) {
	$scope.alerts = [];
	$http.get('rest/repository/list').success(function(data) {
		$scope.repositories = data;
	});

	$scope.addRepository = function() {
		var modalInstance = $modal.open({
			templateUrl : 'dialogs/repositories-add.html',
			controller : 'RepositoriesAddController'
		});

		modalInstance.result.then(function(data) {
			var repositories = $scope.repositories
			repositories.push(data)
			$scope.repositories = repositories
		}, function(data) {
			$scope.alerts = [];
		});
	}
	
	$scope.deleteRepository = function(repository) {
		var config = {
			params: repository
		}
		$http.delete('rest/repository/delete', config).success(
			function(data) {
				var repositories = $.grep($scope.repositories, 
					function(repository) { 
					    return repository.location == data.location
					}
				, true)
				$scope.repositories = repositories
			}
		);
	}
});

gitistics.controller('RepositoriesAddController', function($scope, $http, $modalInstance) {
	$scope.repository = {};
	$scope.ok = function(repository) {
		$http.post('rest/repository/add', repository).success(
			function(data) {
				$modalInstance.close(data);	
			});
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	}
});
