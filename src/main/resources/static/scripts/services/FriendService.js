angular.module('wwwApp').factory('FriendService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/friend/:Id',{ Id: '@Id' });
}]);