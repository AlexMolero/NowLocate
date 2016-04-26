'use strict';

angular.module('nowLocateApp')
	.controller('TemperaturaDeleteController', function($scope, $uibModalInstance, entity, Temperatura) {

        $scope.temperatura = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Temperatura.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
