'use strict';

angular.module('clinicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('area', {
                parent: 'entity',
                url: '/areas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Areas'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/area/areas.html',
                        controller: 'AreaController'
                    }
                },
                resolve: {
                }
            })
            .state('area.detail', {
                parent: 'entity',
                url: '/area/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Area'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/area/area-detail.html',
                        controller: 'AreaDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Area', function($stateParams, Area) {
                        return Area.get({id : $stateParams.id});
                    }]
                }
            })
            .state('area.new', {
                parent: 'area',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/area/area-dialog.html',
                        controller: 'AreaDialogController',
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
                        $state.go('area', null, { reload: true });
                    }, function() {
                        $state.go('area');
                    })
                }]
            })
            .state('area.edit', {
                parent: 'area',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/area/area-dialog.html',
                        controller: 'AreaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Area', function(Area) {
                                return Area.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('area', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
