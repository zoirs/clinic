'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            //.state('arexsaa', {
            //    parent: 'entity',
            //    url: '/arexsaas',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //        pageTitle: 'Areas'
            //    },
            //    views: {
            //        'content@': {
            //            templateUrl: 'scripts/app/entities/area/areas.html',
            //            controller: 'AreaController'
            //        }
            //    },
            //    resolve: {}
            //})

            .state('find', {
                parent: 'entity',
                url: '/find/{type}/{metro}/{speciality}',
                data: {
                    pageTitle: 'Area'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/search/found.html',
                        controller: 'FoundController'
                    }
                }
            })
    });
