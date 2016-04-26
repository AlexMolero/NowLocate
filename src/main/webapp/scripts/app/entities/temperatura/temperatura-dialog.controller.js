'use strict';

angular.module('nowLocateApp').controller('TemperaturaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Temperatura', 'Expedicion',
        function($scope, $stateParams, $uibModalInstance, entity, Temperatura, Expedicion) {

        $scope.temperatura = entity;
        $scope.expedicions = Expedicion.query();
        $scope.load = function(id) {
            Temperatura.get({id : id}, function(result) {
                $scope.temperatura = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('nowLocateApp:temperaturaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.temperatura.id != null) {
                Temperatura.update($scope.temperatura, onSaveSuccess, onSaveError);
            } else {
                Temperatura.save($scope.temperatura, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForHora = {};

        $scope.datePickerForHora.status = {
            opened: false
        };

        $scope.datePickerForHoraOpen = function($event) {
            $scope.datePickerForHora.status.opened = true;
        };
}]);
