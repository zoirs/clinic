'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('diagnostic', {
                parent: 'entity',
                url: '/diagnostics',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Diagnostics'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/diagnostic/diagnostics.html',
                        controller: 'DiagnosticController'
                    }
                },
                resolve: {
                }
            })
            .state('diagnostic.detail', {
                parent: 'entity',
                url: '/diagnostic/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Diagnostic'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/diagnostic/diagnostic-detail.html',
                        controller: 'DiagnosticDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Diagnostic', function($stateParams, Diagnostic) {
                        return Diagnostic.get({id : $stateParams.id});
                    }]
                }
            })
            .state('diagnostic.new', {
                parent: 'diagnostic',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/diagnostic/diagnostic-dialog.html',
                        controller: 'DiagnosticDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    alias: null,
                                    docdocId: null,
                                    lastUpdated: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('diagnostic', null, { reload: true });
                    }, function() {
                        $state.go('diagnostic');
                    })
                }]
            })
            .state('diagnostic.edit', {
                parent: 'diagnostic',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/diagnostic/diagnostic-dialog.html',
                        controller: 'DiagnosticDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Diagnostic', function(Diagnostic) {
                                return Diagnostic.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('diagnostic', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
