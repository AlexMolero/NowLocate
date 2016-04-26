'use strict';

angular.module('nowLocateApp')
	.controller('ExpedicionDeleteController', function($scope, $uibModalInstance, entity, Expedicion) {

        $scope.expedicion = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.load = function (id) {
            Expedicion.get({id: id}, function(result) {
                $scope.expedicion = result;
            });
        };
       /* var unsubscribe = $rootScope.$on('nowLocateApp:expedicionUpdate', function(event, result) {
            $scope.expedicion = result;
        });
        $scope.$on('$destroy', unsubscribe);

*/
    });
