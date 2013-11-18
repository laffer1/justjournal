angular.module('wwwApp').factory('TagService', ['$resource', function ($resource) {
        'use strict';
        return $resource('/api/tags/:TagId',{ TagId: '@TagId' });
}]);