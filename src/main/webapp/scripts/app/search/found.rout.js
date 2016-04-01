'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider

            .state('home.find', {
                url: '/find/{type}/{metro}/{speciality}',
                data: {
                    pageTitle: 'Найдено'
                },
                views: {
                    'dataTemplate': {
                        templateUrl: 'scripts/app/search/found.html',
                        controller: 'FoundController'
                    }
                }
            })
    });
