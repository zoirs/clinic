'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider

            .state('home.spec-list', {
                url: '/',
                data: {
                    pageTitle: 'Бла бла'
                },
                views: {
                    'dataTemplate': {
                        templateUrl: 'scripts/app/speciality-list/spec-list.html',
                        controller: 'SpecListController'
                    }
                }
            })
    });
