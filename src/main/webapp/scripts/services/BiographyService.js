angular.module('wwwApp').factory('BiographyService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/biography/:Id', { Id: '@Id' });
}]);