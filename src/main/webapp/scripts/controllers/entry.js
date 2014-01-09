angular.module('wwwApp').controller('EntryCtrl', ['$scope', '$routeParams', 'MoodService', 'LocationService', 'SecurityService', 'EntryService',
    function ($scope, $routeParams, MoodService, LocationService, SecurityService, EntryService) {
        'use strict';

        $scope.moods = MoodService.query();
        $scope.locations = LocationService.query();
        $scope.security = SecurityService.query();
        $scope.entry = {
            date: new Date() // TODO: is this the right format?
        };
        $scope.ErrorMessage = '';

        $scope.cancel = function() {
          // TODO: something
        };

        $scope.save = function () {
            if (jQuery('form#UpdateJournal').valid()) {
                // EDIT case
                if (typeof $routeParams.entryId !== 'undefined') {
                    EntryService.update($scope.entry, function success() {
                                //$location.path('/users/');  // TODO: username
                                alert('Blog Entry Updated');
                            },
                            function fail(response) {
                                if (typeof(response.data.ModelState) !== 'undefined') {
                                    $scope.ErrorMessage = response.data.Message + ' (' + response.status + ') ' + angular.toJson(response.data.ModelState);
                                }
                                else if (typeof(response.data.ExceptionMessage) !== 'undefined') {
                                    $scope.ErrorMessage = response.data.Message + ' (' + response.status + ') ' + response.data.ExceptionMessage + ' ' + response.data.ExceptionType + ' ' + response.data.StackTrace;
                                }
                                else {
                                    $scope.ErrorMessage = 'Unknown error occurred. Response was ' + angular.toJson(response);
                                }
                            }
                    );
                    return;
                }

                EntryService.save($scope.entry, function success() {
                            //$location.path('/users/');  // TODO: username
                            alert('Blog Entry Posted');
                        },
                        function fail(response) {
                            if (typeof(response.data.ModelState) !== 'undefined') {
                                $scope.ErrorMessage = response.data.Message + ' (' + response.status + ') ' + angular.toJson(response.data.ModelState);
                            }
                            else if (typeof(response.data.ExceptionMessage) !== 'undefined') {
                                $scope.ErrorMessage = response.data.Message + ' (' + response.status + ') ' + response.data.ExceptionMessage + ' ' + response.data.ExceptionType + ' ' + response.data.StackTrace;
                            }
                            else {
                                $scope.ErrorMessage = 'Unknown error occurred. Response was ' + angular.toJson(response);
                            }
                        }
                );
            }
        };

        $scope.$on('$destroy', function finalize() {
            $scope.moods = null;
            $scope.locations = null;
            $scope.security = null;
            $scope.entry = null;
            $scope.ErrorMessage = null;
        });

        $scope.init = function () {
            jQuery("#frmUpdateJournal").validate({
                submitHandler: function (form) {
                    form.submit();
                }
            });

            jQuery('#tags').bind('change', function () {
                $(this).value = $(this).value.toLocaleLowerCase();
            });

            if (typeof $routeParams.entryId !== 'undefined') {
                $scope.entry = EntryService.get({Id: $routeParams.entryId});
            }
        };
        $scope.init();
    }]);