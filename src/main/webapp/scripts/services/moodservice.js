angular.module('wwwApp').factory('MoodService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/mood/:MoodId',{ MoodId: '@MoodId' });
}]);