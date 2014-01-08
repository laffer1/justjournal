angular.module('wwwApp').factory('AccountService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/account/:Id',{ Id: '@Id' });
}]);