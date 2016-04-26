'use strict';

angular.module('nowLocateApp')
	.controller('CamionDeleteController', function($scope, $uibModalInstance, entity, Camion) {

        $scope.camion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Camion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
