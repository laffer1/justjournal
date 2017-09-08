angular.module('wwwApp').controller('ProfileCtrl', ['$scope', '$routeParams', 'AccountService',
    'BiographyService', 'StatisticsService', 'FriendService', 'JournalService',
    function ($scope, $routeParams, AccountService, BiographyService, StatisticsService, FriendService, JournalService) {
        'use strict';

        $scope.username = $routeParams.username;

        $scope.account = AccountService.get({Id: $scope.username});

        $scope.journal = JournalService.byuser({User: $scope.username});

        $scope.stats = StatisticsService.get({Id: $scope.username});

        $scope.friends = FriendService.query({Id: $scope.username});

        $scope.biography = BiographyService.get({Id: $scope.username});
    }]);