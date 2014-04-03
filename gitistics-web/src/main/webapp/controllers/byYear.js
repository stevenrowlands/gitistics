'use strict';

var gitistics = angular.module('gitistics');

gitistics.controller('ByYearController', function($scope, $modal, $http) {
	$http.get('rest/statistics/byYear').success(
			function(data) {
				$scope.statistics = data;

				var chartData = new google.visualization.DataTable();
				var chart = new google.visualization.LineChart(document
						.getElementById('chartdiv'));
				chartData.addColumn('number', 'Year');
				chartData.addColumn('number', 'Lines Added');
				chartData.addColumn('number', 'Lines Removed');
				for ( var i in data) {
					var item = data[i];
					chartData.addRow([ item.year, item.linesAdded, item.linesRemoved ])
				}
				chart.draw(chartData, {});
			});
});
