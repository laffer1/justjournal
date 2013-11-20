angular.module('wwwApp').controller('ProfileCtrl', ['$scope', '$routeParams', 'AccountService', 'StatisticsService',
    'FriendService',
    function ($scope, $routeParams, AccountService, StatisticsService, FriendService) {
        'use strict';

        $scope.username = $routeParams.username;

        $scope.account = AccountService.get({Id: $scope.username});

        $scope.stats = StatisticsService.get({Id: $scope.username});

        $scope.friends = FriendService.get({Id: $scope.username});
    }]);