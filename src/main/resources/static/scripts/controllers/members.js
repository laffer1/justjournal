angular.module('wwwApp').controller('MembersCtrl', ['$scope', 'MemberService', function ($scope, MemberService) {
  'use strict';

    $scope.Members = MemberService.query();
}]);