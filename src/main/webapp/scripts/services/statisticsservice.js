angular.module('wwwApp').factory('StatisticsService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/statistics/:Id', { Id: '@Id' });
}]);