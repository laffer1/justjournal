angular.module('wwwApp').factory('MemberService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/members?id:MemberId',{ MemberId: '@MemberId' });
}]);