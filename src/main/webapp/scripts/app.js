'use strict';

angular.module('wwwApp', [])
        .config(function ($routeProvider) {
            $routeProvider
                    .when('/', {
                        templateUrl: 'views/main.html',
                        controller: 'MainCtrl'
                    })
                    .when('/sitemap', {
                        templateUrl: 'views/sitemap.html',
                        controller: 'SitemapCtrl'
                    })
                    .when('/tags', {
                        templateUrl: 'views/tags.html',
                        controller: 'TagsCtrl'
                    })
                    .otherwise({
                        redirectTo: '/'
                    });
        });
