angular.module('wwwApp').controller('ProfileCtrl', ['$scope', '$routeParams', 'AccountService',
    'BiographyService', 'StatisticsService', 'FriendService',
    function ($scope, $routeParams, AccountService, BiographyService, StatisticsService, FriendService) {
        'use strict';

        $scope.username = $routeParams.username;

        $scope.account = AccountService.get({Id: $scope.username});

        $scope.stats = StatisticsService.get({Id: $scope.username});

        $scope.friends = FriendService.get({Id: $scope.username});

        $scope.biography = BiographyService.get({Id: $scope.username});
    }]);