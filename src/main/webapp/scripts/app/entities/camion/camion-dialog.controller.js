'use strict';

angular.module('nowLocateApp').controller('CamionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Camion', 'Expedicion',
        function($scope, $stateParams, $uibModalInstance, entity, Camion, Expedicion) {

        $scope.camion = entity;
        $scope.expedicions = Expedicion.query();
        $scope.load = function(id) {
            Camion.get({id : id}, function(result) {
                $scope.camion = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('nowLocateApp:camionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.camion.id != null) {
                Camion.update($scope.camion, onSaveSuccess, onSaveError);
            } else {
                Camion.save($scope.camion, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
