'use strict';

angular.module('nowLocateApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('camion', {
                parent: 'entity',
                url: '/camions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.camion.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/camion/camions.html',
                        controller: 'CamionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('camion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('camion.detail', {
                parent: 'entity',
                url: '/camion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'nowLocateApp.camion.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/camion/camion-detail.html',
                        controller: 'CamionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('camion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Camion', function($stateParams, Camion) {
                        return Camion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('camion.new', {
                parent: 'camion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/camion/camion-dialog.html',
                        controller: 'CamionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    matricula: null,
                                    volumen: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('camion', null, { reload: true });
                    }, function() {
                        $state.go('camion');
                    })
                }]
            })
            .state('camion.edit', {
                parent: 'camion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/camion/camion-dialog.html',
                        controller: 'CamionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Camion', function(Camion) {
                                return Camion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('camion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('camion.delete', {
                parent: 'camion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/camion/camion-delete-dialog.html',
                        controller: 'CamionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Camion', function(Camion) {
                                return Camion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('camion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
