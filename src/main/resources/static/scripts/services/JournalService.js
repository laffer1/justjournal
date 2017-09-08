angular.module('wwwApp').factory('JournalService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/journal/:Id', {Id: '@Id', User: '@User'},
            {
                'byuser': {
                    method: 'GET',
                    isArray: true,
                    url: '/api/journal/user/:User'
                }
            });
}]);