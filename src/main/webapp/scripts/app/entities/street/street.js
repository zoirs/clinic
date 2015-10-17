'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('street', {
                parent: 'entity',
                url: '/streets',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Streets'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/street/streets.html',
                        controller: 'StreetController'
                    }
                },
                resolve: {
                }
            })
            .state('street.detail', {
                parent: 'entity',
                url: '/street/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Street'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/street/street-detail.html',
                        controller: 'StreetDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Street', function($stateParams, Street) {
                        return Street.get({id : $stateParams.id});
                    }]
                }
            })
            .state('street.new', {
                parent: 'street',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/street/street-dialog.html',
                        controller: 'StreetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    alias: null,
                                    docdocId: null,
                                    lastUpdate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('street', null, { reload: true });
                    }, function() {
                        $state.go('street');
                    })
                }]
            })
            .state('street.edit', {
                parent: 'street',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/street/street-dialog.html',
                        controller: 'StreetDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Street', function(Street) {
                                return Street.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('street', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
