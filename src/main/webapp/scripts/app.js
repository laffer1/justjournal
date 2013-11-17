'use strict';

angular.module('wwwApp', [])
        .config(function ($routeProvider) {
            $routeProvider
                    .when('/', {
                        templateUrl: 'views/main.html',
                        controller: 'MainCtrl'
                    })
                    .when('/cancel', {
                        templateUrl: 'views/cancel.html',
                        controller: 'CancelCtrl'
                    })
                    .when('/members', {
                        templateUrl: 'views/members.html',
                        controller: 'MembersCtrl'
                    })
                    .when('/moodlist',{
                        templateUrl: 'views/moodlist.html',
                        controller: 'MoodlistCtrl'
                    })
                    .when('/privacy', {
                        templateUrl: 'views/privacy.html',
                        controller: 'PrivacyCtrl'
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
