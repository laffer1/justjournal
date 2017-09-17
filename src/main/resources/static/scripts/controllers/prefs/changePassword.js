angular.module('wwwApp').controller('PrefsChangePasswordCtrl', ['$scope', 'AccountService',
    function ($scope, AccountService) {
        'use strict';

        $scope.ErrorMessage = '';

        $scope.password = '';
        $scope.newPassword = '';

        $scope.save = function () {
            ga('send', 'event', 'Authentication', 'PasswordReset');
            $scope.result = AccountService.password({
                        passCurrent: $scope.password,
                        passNew: $scope.newPassword
                    },
                    function success() {
                        alert('Password Changed');
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