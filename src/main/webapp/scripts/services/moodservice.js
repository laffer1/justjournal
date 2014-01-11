angular.module('wwwApp').factory('MoodService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/mood?id:MoodId',{ MoodId: '@MoodId' });
}]);