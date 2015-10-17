'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('clinic', {
                parent: 'entity',
                url: '/clinics',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Clinics'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/clinic/clinics.html',
                        controller: 'ClinicController'
                    }
                },
                resolve: {
                }
            })
            .state('clinic.detail', {
                parent: 'entity',
                url: '/clinic/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Clinic'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/clinic/clinic-detail.html',
                        controller: 'ClinicDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Clinic', function($stateParams, Clinic) {
                        return Clinic.get({id : $stateParams.id});
                    }]
                }
            })
            .state('clinic.new', {
                parent: 'clinic',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/clinic/clinic-dialog.html',
                        controller: 'ClinicDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    shortName: null,
                                    alias: null,
                                    url: null,
                                    longitude: null,
                                    latitude: null,
                                    streetName: null,
                                    house: null,
                                    description: null,
                                    weekdaysOpen: null,
                                    weekendOpen: null,
                                    shortDescription: null,
                                    isDiagnostic: null,
                                    isClinic: null,
                                    isDoctor: null,
                                    phoneContact: null,
                                    phoneAppointmen: null,
                                    phoneReplacement: null,
                                    logoPath: null,
                                    logo: null,
                                    scheduleStateOnline: null,
                                    email: null,
                                    minPrice: null,
                                    maxPrice: null,
                                    docdocId: null,
                                    lastUpdated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('clinic', null, { reload: true });
                    }, function() {
                        $state.go('clinic');
                    })
                }]
            })
            .state('clinic.edit', {
                parent: 'clinic',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/clinic/clinic-dialog.html',
                        controller: 'ClinicDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Clinic', function(Clinic) {
                                return Clinic.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('clinic', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
