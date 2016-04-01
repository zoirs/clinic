'use strict';

angular.module('clinicApp')
    //.config(function ($stateProvider) {
    //    $stateProvider
    //        .state('home', {
    //            parent: 'site',
    //            url: '/',
    //            data: {
    //                authorities: []
    //            },
    //            views: {
    //                'content@': {
    //                    templateUrl: 'scripts/app/main/main.html',
    //                    controller: 'MainController'
    //                }
    //            }
    //        });
    //});

    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                //abstract: true,
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                }
            });
    });
