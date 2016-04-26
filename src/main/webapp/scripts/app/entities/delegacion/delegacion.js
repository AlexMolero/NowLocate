'use strict';

angular.module('nowLocateApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('delegacion', {
                parent: 'entity',
                url: '/delegacions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.delegacion.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/delegacion/delegacions.html',
                        controller: 'DelegacionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('delegacion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('delegacion.detail', {
                parent: 'entity',
                url: '/delegacion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.delegacion.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/delegacion/delegacion-detail.html',
                        controller: 'DelegacionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('delegacion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Delegacion', function($stateParams, Delegacion) {
                        return Delegacion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('delegacion.new', {
                parent: 'delegacion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/delegacion/delegacion-dialog.html',
                        controller: 'DelegacionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    localidad: null,
                                    volumenAlmacen: null,
                                    calle: null,
                                    latitud: null,
                                    longitud: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('delegacion', null, { reload: true });
                    }, function() {
                        $state.go('delegacion');
                    })
                }]
            })
            .state('delegacion.edit', {
                parent: 'delegacion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/delegacion/delegacion-dialog.html',
                        controller: 'DelegacionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Delegacion', function(Delegacion) {
                                return Delegacion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('delegacion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('delegacion.delete', {
                parent: 'delegacion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/delegacion/delegacion-delete-dialog.html',
                        controller: 'DelegacionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Delegacion', function(Delegacion) {
                                return Delegacion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('delegacion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
