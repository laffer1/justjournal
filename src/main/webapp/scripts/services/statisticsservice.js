angular.module('wwwApp').factory('StatisticsService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/statistics?id:Id',{ Id: '@Id' });
}]);