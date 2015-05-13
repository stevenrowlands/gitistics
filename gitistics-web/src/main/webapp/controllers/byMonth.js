'use strict';

var gitistics = angular.module('gitistics');

gitistics.controller('ByMonthController', function($scope, $modal, $routeParams, $http) {

	var params = { 
		repositoryName : $routeParams.repositoryName,
	    groups : [ "YEAR", "MONTH"], 
	    orders : [ {order : "YEAR", direction : "DESC"}, {order : "MONTH", direction : "DESC"}] 
    }
	
	$scope.repository = $routeParams.repositoryName;
	
	$http.post('rest/statistics/statistics', params).success(
			function(data) {
				$scope.statistics = data;

				var chartData = new google.visualization.DataTable();
				var chart = new google.visualization.ComboChart(document.getElementById('chartdiv'));
				chartData.addColumn('string', 'Month');
				chartData.addColumn('number', 'Lines Added');
				chartData.addColumn('number', 'Lines Removed');
				chartData.addColumn('number', 'Commits');
				for ( var i in data) {
					var item = data[i];
					chartData.addRow([ item.year + "-" + item.month, item.linesAdded, item.linesRemoved, item.commits ])
				}
				var options = {
			    		vAxis: [
							{title: "Lines" },
							{title: "Commits"}
						],
			    		hAxis: {title: "Year"},
			    		seriesType: "line",
				    	series:{
				    		0:{type:"line", targetAxisIndex:0},
				    		1:{type:"line", targetAxisIndex:0},
				    		2:{type:"bars", targetAxisIndex:1},
				    	}
                 };

				chart.draw(chartData, options);
			});
});
