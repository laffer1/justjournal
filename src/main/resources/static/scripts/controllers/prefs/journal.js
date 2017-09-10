angular.module('wwwApp').controller('PrefsJournalCtrl', ['$scope', 'LoginService', 'JournalService', 'StyleService',
    function ($scope, LoginService, JournalService, StyleService) {
        'use strict';

        $scope.ErrorMessage = '';
        $scope.journal = {
            
        };

        $scope.login = LoginService.get(null, function (login) {
             JournalService.get({Id: login.username}, function(journal) {
                 $scope.journal = journal;
             });
        });

        $scope.styles = StyleService.query();

        $scope.save = function () {
            $scope.result = JournalService.save($scope.journal,
                    function success() {
                        alert('Journal Changed');
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