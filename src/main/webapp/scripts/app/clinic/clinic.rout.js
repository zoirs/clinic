'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider

            .state('home.clinic_show', {
                url: '/clinic/{id}/{name}',
                data: {
                    pageTitle: 'Клиника'
                },
                views: {
                    'dataTemplate': {
                        templateUrl: 'scripts/app/clinic/clinic.show.html',
                        controller: 'ClinicShowController'
                    }
                } ,
                resolve: {
                    entity: ['$stateParams', 'Clinic', function($stateParams, Clinic) {
                        return Clinic.get({id : $stateParams.id});
                    }]
                }
            })
    });

