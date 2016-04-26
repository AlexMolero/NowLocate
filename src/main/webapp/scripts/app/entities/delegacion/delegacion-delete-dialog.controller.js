'use strict';

angular.module('nowLocateApp')
	.controller('DelegacionDeleteController', function($scope, $uibModalInstance, entity, Delegacion) {

        $scope.delegacion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Delegacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
