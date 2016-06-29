angular.module('wwwApp',
                [
                    'ui',
                    'ui.bootstrap.pagination',
                    'ngRoute',
                    'ngResource',
                    'ngCookies',
                    'ngRoute',
                    'ngSanitize'
                ])
        .config(function ($routeProvider, $locationProvider) {
            'use strict';

            $routeProvider
                    .when('/', {
                        templateUrl: 'views/main.html',
                        controller: 'MainCtrl'
                    })
                    .when('/cancel', {
                        templateUrl: 'views/cancel.html',
                        controller: 'CancelCtrl'
                    })
                    .when('/entry/:entryId', {
                        templateUrl: 'views/entry.html',
                        controller: 'EntryCtrl'
                    })
                    .when('/entry', {
                        templateUrl: 'views/entry.html',
                        controller: 'EntryCtrl'
                    })
                    .when('/members', {
                        templateUrl: 'views/members.html',
                        controller: 'MembersCtrl'
                    })
                    .when('/moodlist',{
                        templateUrl: 'views/moodlist.html',
                        controller: 'MoodListCtrl'
                    })
                    .when('/privacy', {
                        templateUrl: 'views/privacy.html',
                        controller: 'PrivacyCtrl'
                    })
                    .when('/profile/:username', {
                        templateUrl: 'views/profile.html',
                        controller: 'ProfileCtrl'
                    })
                    .when('/search', {
                        templateUrl: 'views/search.html',
                        controller: 'SearchCtrl'
                    })
                    .when('/sitemap', {
                        templateUrl: 'views/sitemap.html',
                        controller: 'SitemapCtrl'
                    })
                    .when('/stats', {
                        templateUrl: 'views/stats.html',
                        controller: 'StatsCtrl'
                    })
                    .when('/support', {
                        templateUrl: 'views/support.html',
                        controller: 'SupportCtrl'
                    })
                    .when('/support/bugs', {
                        templateUrl: 'views/bugs.html',
                        controller: 'BugsCtrl'
                    })
                    .when('/support/faq', {
                        templateUrl: 'views/faq.html',
                        controller: 'FaqCtrl'
                    })
                    .when('/support/tech', {
                        templateUrl: 'views/tech.html',
                        controller: 'TechCtrl'
                    })
                    .when('/tags', {
                        templateUrl: 'views/tags.html',
                        controller: 'TagsCtrl'
                    })
                    .when('/users', {

                    })
                    .otherwise({
                        redirectTo: '/'
                    });

            $locationProvider.hashPrefix('!');
        });