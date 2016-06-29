angular.module('wwwApp').controller('StatsCtrl', ['$scope', 'StatisticsService', function ($scope, StatisticsService) {
  'use strict';

    $scope.stats = StatisticsService.query();
}]);