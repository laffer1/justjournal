angular.module('wwwApp').factory('SearchService', ['$resource', function ($resource) {
    'use strict';
    return $resource('/api/search?term=:term', {term: '@term'});
}]);