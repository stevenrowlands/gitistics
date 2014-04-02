'use strict';

var gitistics = angular.module('gitistics', [ 'ui.bootstrap', 'ngRoute']);

google.load('visualization', '1', {
	packages : [ 'corechart' ]
});