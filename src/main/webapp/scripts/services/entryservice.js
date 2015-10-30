angular.module('wwwApp').factory('EntryService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/entry/:Id', {Id: '@Id'},
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