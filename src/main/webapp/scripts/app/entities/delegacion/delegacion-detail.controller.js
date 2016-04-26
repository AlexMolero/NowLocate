'use strict';

angular.module('nowLocateApp')
    .controller('DelegacionDetailController', function ($scope, $rootScope, $stateParams, entity, Delegacion, Expedicion) {
        $scope.delegacion = entity;
        $scope.load = function (id) {
            Delegacion.get({id: id}, function(result) {
                $scope.delegacion = result;
            });
        };
        var unsubscribe = $rootScope.$on('nowLocateApp:delegacionUpdate', function(event, result) {
            $scope.delegacion = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
