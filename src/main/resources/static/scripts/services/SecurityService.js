angular.module('wwwApp').factory('SecurityService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/security/:Id',{ Id: '@Id' });
}]);