'use strict';

angular.module('nowLocateApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expedicion', {
                parent: 'entity',
                url: '/expedicions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.expedicion.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expedicion/expedicions.html',
                        controller: 'ExpedicionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expedicion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expedicion.detail', {
                parent: 'entity',
                url: '/expedicion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.expedicion.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expedicion/expedicion-detail.html',
                        controller: 'ExpedicionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expedicion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Expedicion', function($stateParams, Expedicion) {
                        return Expedicion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('expedicion.new', {
                parent: 'expedicion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expedicion/expedicion-dialog.html',
                        controller: 'ExpedicionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fechaInicio: null,
                                    fechaEntrega: null,
                                    frigorifico: null,
                                    tempMax: null,
                                    tempMin: null,
                                    descripcion: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expedicion', null, { reload: true });
                    }, function() {
                        $state.go('expedicion');
                    })
                }]
            })
            .state('expedicion.edit', {
                parent: 'expedicion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expedicion/expedicion-dialog.html',
                        controller: 'ExpedicionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Expedicion', function(Expedicion) {
                                return Expedicion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expedicion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('expedicion.delete', {
                parent: 'expedicion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/expedicion/expedicion-delete-dialog.html',
                        controller: 'ExpedicionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Expedicion', function(Expedicion) {
                                return Expedicion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expedicion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('expedicion.showMap', {
            parent: 'expedicion',
            url: '/{id}/show',
            data: {
                authorities: ['ROLE_USER'],
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/expedicion/routeTruck.html',
                    controller: 'RouteTruckController',
                    size: 'md',
                    resolve: {
                        entity: ['Expedicion', function(Expedicion) {
                            return Expedicion.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function(result) {
                    $state.go('expedicion', null, { reload: true });
                }, function() {
                    $state.go('^');
                })
            }]
        });
    });
