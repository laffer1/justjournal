angular.module('wwwApp').factory('CommentService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/comment/:Id', {Id: '@Id', User: '@User', EntryId: '@EntryId'},
            {
                'update': {
                    method: 'PUT'
                },
                'queryByEntry': {
                    method: 'GET',
                    isArray: true,
                    url: '/api/comment/?entryId=:EntryId'
                }
            });
}]);