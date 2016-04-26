'use strict';

angular.module('nowLocateApp')
    .controller('TemperaturaDetailController', function ($scope, $rootScope, $stateParams, entity, Temperatura, Expedicion) {
        $scope.temperatura = entity;
        $scope.load = function (id) {
            Temperatura.get({id: id}, function(result) {
                $scope.temperatura = result;
            });
        };
        var unsubscribe = $rootScope.$on('nowLocateApp:temperaturaUpdate', function(event, result) {
            $scope.temperatura = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
