angular.module('wwwApp').factory('EntryService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/entry/:Id', {Id: '@Id', User: '@User'},
            {
                'update': {
                    method: 'PUT'
                },
                'get': {
                    method: 'GET',
                    isArray: false,
                    url: '/api/entry/:User/eid/:Id'
                }
            });
}]);