angular.module('wwwApp').factory('LocationService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/location/:Id',{ Id: '@Id' });
}]);