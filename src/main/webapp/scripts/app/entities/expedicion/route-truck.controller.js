'use strict';

angular.module('nowLocateApp')
	.controller('RouteTruckController', function($scope, $uibModalInstance,$stateParams, Expedicion, entity) {

        $scope.expedicion = entity;
        $scope.load = function (id) {
            Expedicion.get({id: id}, function(result) {
                $scope.expedicion = result;
            });
        };
    });
