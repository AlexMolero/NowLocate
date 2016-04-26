'use strict';

angular.module('nowLocateApp')
    .controller('CamionDetailController', function ($scope, $rootScope, $stateParams, entity, Camion, Expedicion) {
        $scope.camion = entity;
        $scope.load = function (id) {
            Camion.get({id: id}, function(result) {
                $scope.camion = result;
            });
        };
        var unsubscribe = $rootScope.$on('nowLocateApp:camionUpdate', function(event, result) {
            $scope.camion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
