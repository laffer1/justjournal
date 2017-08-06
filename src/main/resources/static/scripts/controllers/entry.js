angular.module('wwwApp').controller('EntryCtrl', ['$scope', '$routeParams', '$location',
    'MoodService', 'LocationService', 'SecurityService', 'EntryService', 'LoginService',
    function ($scope, $routeParams, $location, MoodService, LocationService, SecurityService, EntryService, LoginService) {
        'use strict';

        CKEDITOR.replace( 'body' );

        $scope.login = LoginService.get({}, function () {
                    if (typeof $routeParams.entryId !== 'undefined') {
                        $scope.entry = EntryService.get({Id: $routeParams.entryId, User: $scope.login.username},
                                function () {
                                    if (typeof $scope.entry.mood !== 'undefined' && typeof $scope.entry.mood.id !== 'undefined')
                                        $scope.entry.mood = $scope.entry.mood.id;

                                    if (typeof $scope.entry.location !== 'undefined' && typeof $scope.entry.location.id !== 'undefined')
                                        $scope.entry.location = $scope.entry.location.id;

                                    if (typeof $scope.entry.security !== 'undefined' && typeof $scope.entry.security.id !== 'undefined')
                                        $scope.entry.security = $scope.entry.security.id;

                                    $scope.entry.allowComments = $scope.entry.allowComments == 'Y';

                                    $scope.entry.emailComments = $scope.entry.emailComments == 'Y';
                                }
                        );
                    }
                }
        );
        $scope.moods = MoodService.query();
        $scope.locations = LocationService.query();
        $scope.security = SecurityService.query();
        $scope.entry = {
            allowComments: true,
            autoFormat: true,
            // date: new Date(), // TODO: is this the right format?
            emailComments: true,
            format: 'MARKDOWN',
            subject: '',
            body: '',
            tag: '',
            music: '',
            trackback: '',
            security: 0,
            mood: 12,
            location: 0
        };
        $scope.ErrorMessage = '';

        $scope.cancel = function () {
            // TODO: what about angular stuff?
            $scope.UpdateJournal.$setPristine();
            $scope.entry = {
                allowComments: true,
                autoFormat: true,
                // date: new Date(), // TODO: is this the right format?
                emailComments: true,
                format: 'MARKDOWN',
                subject: '',
                body: '',
                tag: '',
                music: '',
                trackback: '',
                security: 0,
                mood: 12,
                location: 0
            };
        };

        $scope.markitup = function() {
            var data = CKEDITOR.instances.body.getData();
            $scope.entry.body = toMarkdown(data);
        };

        $scope.save = function () {
            if (typeof $scope.entry === 'undefined') {
                $scope.ErrorMessage = 'Unknown error occurred.';
                return false;
            }

            /*if (typeof $scope.entry.mood !== 'undefined' && typeof $scope.entry.mood.id !== 'undefined')
             $scope.entry.mood = $scope.entry.mood.id;

             if (typeof $scope.entry.location !== 'undefined' && typeof $scope.entry.location.id !== 'undefined')
             $scope.entry.location = $scope.entry.location.id;

             if (typeof $scope.entry.security !== 'undefined' && typeof $scope.entry.security.id !== 'undefined')
             $scope.entry.security = $scope.entry.security.id;  */

            $scope.markitup();

            $scope.entry.tags = [];
            if (typeof $scope.entry.tag !== 'undefined' && $scope.entry.tag.length > 0) {
                $scope.entry.tag = $scope.entry.tag.toLowerCase();
                $scope.entry.tag = $scope.entry.tag.replace(' ', ',');

                $scope.entry.tags = $scope.entry.tag.split(',');
            }

            // EDIT case
            if (typeof $routeParams.entryId !== 'undefined') {
                EntryService.update($scope.entry, function success() {
                            window.location.href = ('users/' + $scope.login.username);
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
                return false;
            }

            EntryService.save($scope.entry, function success() {
                        window.location.href = ('users/' + $scope.login.username);
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
            return false;
        };

        $scope.tagclean = function () {
            $scope.entry.tag = $scope.entry.tag.toLowerCase();
            $scope.entry.tag = $scope.entry.tag.replace(' ', ',');
        };

        $scope.$on('$destroy', function finalize() {
            $scope.moods = null;
            $scope.locations = null;
            $scope.security = null;
            $scope.entry = null;
            $scope.ErrorMessage = null;
        });
    }]);
