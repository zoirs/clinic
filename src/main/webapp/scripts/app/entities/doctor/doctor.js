'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('doctor', {
                parent: 'entity',
                url: '/doctors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Doctors'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/doctor/doctors.html',
                        controller: 'DoctorController'
                    }
                },
                resolve: {
                }
            })
            .state('doctor.detail', {
                parent: 'entity',
                url: '/doctor/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Doctor'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/doctor/doctor-detail.html',
                        controller: 'DoctorDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Doctor', function($stateParams, Doctor) {
                        return Doctor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('doctor.new', {
                parent: 'doctor',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/doctor/doctor-dialog.html',
                        controller: 'DoctorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fio: null,
                                    alias: null,
                                    rating: null,
                                    ratingInternal: null,
                                    priceFirst: null,
                                    priceSpecial: null,
                                    sex: null,
                                    img: null,
                                    reviewCount: null,
                                    textAbout: null,
                                    experiencaYear: null,
                                    departure: null,
                                    category: null,
                                    degree: null,
                                    rank: null,
                                    extra: null,
                                    isActive: null,
                                    docdocId: null,
                                    lastUpdated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('doctor', null, { reload: true });
                    }, function() {
                        $state.go('doctor');
                    })
                }]
            })
            .state('doctor.edit', {
                parent: 'doctor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/doctor/doctor-dialog.html',
                        controller: 'DoctorDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Doctor', function(Doctor) {
                                return Doctor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('doctor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
