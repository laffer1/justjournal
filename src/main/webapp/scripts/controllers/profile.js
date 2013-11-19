angular.module('wwwApp').controller('ProfileCtrl', ['$scope', '$routeParams', 'AccountService',
    function ($scope, $routeParams, AccountService) {
  'use strict';

    $scope.username = $routeParams.username;

    $scope.account = AccountService.get({Id: $scope.username});
}]);