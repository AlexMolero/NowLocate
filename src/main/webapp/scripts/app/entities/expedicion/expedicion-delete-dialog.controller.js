'use strict';

angular.module('nowLocateApp')
	.controller('ExpedicionDeleteController', function($scope, $uibModalInstance, entity, Expedicion) {

        $scope.expedicion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Expedicion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
