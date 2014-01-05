/*
 * Copyright (c) 2014 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

angular.module('wwwApp').controller('EntryCtrl', ['$scope', 'MoodService', 'LocationService', 'SecurityService', 'EntryService',
    function ($scope, MoodService, LocationService, SecurityService, EntryService) {
        'use strict';

        $scope.moods = MoodService.query();
        $scope.locations = LocationService.query();
        $scope.security = SecurityService.query();
        $scope.entry = {
            date: new Date() // TODO: is this the right format?
        };
        $scope.ErrorMessage = '';

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