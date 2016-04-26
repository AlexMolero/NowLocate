'use strict';

angular.module('nowLocateApp')
    .controller('ExpedicionDetailController', function ($scope, $rootScope, $stateParams, entity, Expedicion, Camion, Delegacion) {
        $scope.expedicion = entity;
        $scope.load = function (id) {
            Expedicion.get({id: id}, function(result) {
                $scope.expedicion = result;
            });
        };
        var unsubscribe = $rootScope.$on('nowLocateApp:expedicionUpdate', function(event, result) {
            $scope.expedicion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
