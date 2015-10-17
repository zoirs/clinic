'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('speciality', {
                parent: 'entity',
                url: '/specialitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Specialitys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/speciality/specialitys.html',
                        controller: 'SpecialityController'
                    }
                },
                resolve: {
                }
            })
            .state('speciality.detail', {
                parent: 'entity',
                url: '/speciality/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Speciality'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/speciality/speciality-detail.html',
                        controller: 'SpecialityDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Speciality', function($stateParams, Speciality) {
                        return Speciality.get({id : $stateParams.id});
                    }]
                }
            })
            .state('speciality.new', {
                parent: 'speciality',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/speciality/speciality-dialog.html',
                        controller: 'SpecialityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    alias: null,
                                    nameGenitive: null,
                                    namePlural: null,
                                    namePluralGenitive: null,
                                    docdocId: null,
                                    lastUpdated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('speciality', null, { reload: true });
                    }, function() {
                        $state.go('speciality');
                    })
                }]
            })
            .state('speciality.edit', {
                parent: 'speciality',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/speciality/speciality-dialog.html',
                        controller: 'SpecialityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Speciality', function(Speciality) {
                                return Speciality.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('speciality', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
