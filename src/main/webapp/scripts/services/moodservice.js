angular.module('wwwApp').factory('MoodService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/moodlist?id:MoodId',{ MoodId: '@MoodId' });
}]);