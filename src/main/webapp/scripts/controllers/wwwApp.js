angular.module('wwwApp').controller('wwwAppCtrl',
        ['$scope', '$routeParams', '$location', 'LoginService',
            function ($scope, $routeParams, $location, LoginService) {
                'use strict';

                $scope.Login = LoginService;

                $scope.$on('$destroy', function finalize() {
                    $scope.Login = null;
                });
            }]);