angular.module('wwwApp').factory('LoginService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/login/:Id',{ Id: '@Id' });
}]);