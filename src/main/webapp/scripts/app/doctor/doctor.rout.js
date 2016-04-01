'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider

            .state('home.doctor_show', {
                url: '/doctor/{id}/{name}',
                data: {
                    pageTitle: 'Доктор'
                },
                views: {
                    'dataTemplate': {
                        templateUrl: 'scripts/app/doctor/doctor.show.html',
                        controller: 'DoctorShowController'
                    }
                } ,
                resolve: {
                    entity: ['$stateParams', 'Doctor', function($stateParams, Doctor) {
                        return Doctor.get({id : $stateParams.id});
                    }]
                }
            })
    });

