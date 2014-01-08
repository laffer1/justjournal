angular.module('wwwApp').controller('MoodListCtrl', ['$scope', 'MoodService', function ($scope, MoodService) {
  'use strict';

    $scope.Moods = MoodService.query();
}]);