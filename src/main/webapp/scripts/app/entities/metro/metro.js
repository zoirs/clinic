'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('metro', {
                parent: 'entity',
                url: '/metros',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Metros'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metro/metros.html',
                        controller: 'MetroController'
                    }
                },
                resolve: {
                }
            })
            .state('metro.detail', {
                parent: 'entity',
                url: '/metro/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Metro'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metro/metro-detail.html',
                        controller: 'MetroDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Metro', function($stateParams, Metro) {
                        return Metro.get({id : $stateParams.id});
                    }]
                }
            })
            .state('metro.new', {
                parent: 'metro',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/metro/metro-dialog.html',
                        controller: 'MetroDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    alias: null,
                                    lineName: null,
                                    lineColor: null,
                                    docdocId: null,
                                    lastUpdated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('metro', null, { reload: true });
                    }, function() {
                        $state.go('metro');
                    })
                }]
            })
            .state('metro.edit', {
                parent: 'metro',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/metro/metro-dialog.html',
                        controller: 'MetroDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Metro', function(Metro) {
                                return Metro.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('metro', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
