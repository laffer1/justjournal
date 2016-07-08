angular.module('wwwApp').controller('CommentCtrl', ['$scope', '$routeParams', '$location',
     'CommentService', 'EntryService', 'LoginService',
    function ($scope, $routeParams, $location, CommentService,
              EntryService, LoginService) {
        'use strict';

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

                                    $scope.entry.autoFormat = $scope.entry.autoFormat == 'Y';

                                    $scope.entry.emailComments = $scope.entry.emailComments == 'Y';
                                }
                        );
                    }
                }
        );

        $scope.comment = {
            subject: '',
            body: '',
            eid: $routeParams.entryId
        };
        $scope.ErrorMessage = '';

        $scope.cancel = function () {
            // TODO: what about angular stuff?
            $scope.AddComment.$setPristine();
            $scope.comment = {
                subject: '',
                body: '',
                eid: $routeParams.entryId
            };
        };

        $scope.save = function () {
            if (jQuery('form#frmAddComment').valid()) {

                if (typeof $scope.comment === 'undefined') {
                    $scope.ErrorMessage = 'Unknown error occurred.';
                    return false;
                }

                CommentService.save($scope.comment, function success() {
                            alert('Comment Posted');
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
            }
            return false;
        };

        $scope.$on('$destroy', function finalize() {
            $scope.comment = null;
            $scope.ErrorMessage = null;
        });

        $scope.init = function () {
            jQuery("#frmAddComment").validate();
        };
        $scope.init();
    }]);