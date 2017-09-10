angular.module('wwwApp').factory('StyleService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/styles/:Id',{ Id: '@Id' });
}]);