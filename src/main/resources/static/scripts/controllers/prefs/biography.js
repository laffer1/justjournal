angular.module('wwwApp').controller('PrefsBiographyCtrl', ['$scope', 'AccountService', 'BiographyService',
    function ($scope, AccountService, BiographyService) {
        'use strict';

        $scope.ErrorMessage = '';

        $scope.biography = '';

        $scope.save = function () {
            $scope.result = BiographyService.save($scope.biography,
                    function success() {
                        alert('Biography Changed');
                    },
                    function fail(response) {
                        if (typeof (response.data.error !== 'undefined')) {
                            $scope.ErrorMessage = response.data.error;
                        }
                        else if (typeof(response.data.ModelState) !== 'undefined') {
                            $scope.ErrorMessage = response.data.Message + ' (' + response.status + ') ' + angular.toJson(response.data.ModelState);
                        }
                        else if (typeof(response.data.ExceptionMessage) !== 'undefined') {
                            $scope.ErrorMessage = response.data.Message + ' (' + response.status + ') ' + response.data.ExceptionMessage + ' ' + response.data.ExceptionType + ' ' + response.data.StackTrace;
                        }
                        else {
                            $scope.ErrorMessage = 'Unknown error occurred. Response was ' + angular.toJson(response);
                        }
                    })
        }
    }]);