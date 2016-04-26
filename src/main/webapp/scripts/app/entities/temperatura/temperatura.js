'use strict';

angular.module('nowLocateApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('temperatura', {
                parent: 'entity',
                url: '/temperaturas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.temperatura.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/temperatura/temperaturas.html',
                        controller: 'TemperaturaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('temperatura');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('temperatura.detail', {
                parent: 'entity',
                url: '/temperatura/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.temperatura.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/temperatura/temperatura-detail.html',
                        controller: 'TemperaturaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('temperatura');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Temperatura', function($stateParams, Temperatura) {
                        return Temperatura.get({id : $stateParams.id});
                    }]
                }
            })
            .state('temperatura.new', {
                parent: 'temperatura',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/temperatura/temperatura-dialog.html',
                        controller: 'TemperaturaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    hora: null,
                                    temperatura: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('temperatura', null, { reload: true });
                    }, function() {
                        $state.go('temperatura');
                    })
                }]
            })
            .state('temperatura.edit', {
                parent: 'temperatura',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/temperatura/temperatura-dialog.html',
                        controller: 'TemperaturaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Temperatura', function(Temperatura) {
                                return Temperatura.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('temperatura', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('temperatura.delete', {
                parent: 'temperatura',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/temperatura/temperatura-delete-dialog.html',
                        controller: 'TemperaturaDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Temperatura', function(Temperatura) {
                                return Temperatura.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('temperatura', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
