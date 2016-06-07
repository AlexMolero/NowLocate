'use strict';

angular.module('nowLocateApp')
    .controller('GraphicTemperatureController', function($scope, $uibModalInstance,$stateParams, Expedicion, Temperatura, entity, temperaturas) {

        $scope.expedicion = entity;

        temperaturas.$promise.then(function(data){
            console.log(data);
            $scope.temperaturas = data;

            $scope.options =
            {
                chart: {
                    type: 'historicalBarChart',
                    height: 300,
                    margin : {
                        top: 20,
                        right: 20,
                        bottom: 65,
                        left: 50
                    },
                    x: function(d){return d[0];},
                    y: function(d){return d[1];},
                    showValues: true,
                    valueFormat: function(d){
                        return d3.format(',.1f')(d);
                    },
                    duration: 100,
                    xAxis: {
                        axisLabel: 'Horas',
                        tickFormat: function(d) {
                            return d
                        },
                        rotateLabels: 30,
                        showMaxMin: false
                    },
                    yAxis: {
                        axisLabel: 'Temperatura',
                        axisLabelDistance: -10,
                        tickFormat: function(d){
                            return d3.format(',.1f')(d);
                        }
                    },
                    tooltip: {
                        keyFormatter: function(d) {
                            return d3.time.format('%x')(new Date(d));
                        }
                    },
                    zoom: {
                        enabled: true,
                        scaleExtent: [1, 10],
                        useFixedDomain: false,
                        useNiceScale: false,
                        horizontalOff: false,
                        verticalOff: true,
                        unzoomEventType: 'dblclick.zoom'
                    }
                }
            };

            //Fin options
            /*$scope.data = [
                {
                    "values" : [ [ 1, 12] , [ 2, 20] , [ 3, 25],[4, 20] ]
                }];*/

            $scope.data = [];
            var cont=0;
            $scope.temperaturas.forEach(function (temperatura) {

                $scope.data.push([
                    cont++,
                     temperatura.temperatura
                ]);
            });
            $scope.bpData = [{
                values: $scope.data
            }];
           /*
            for (var i = 0; i < $scope.temperaturas.length; i++) {
                //$scope.temperaturas[i][2]
                data.push({
                    values: [i][12]
                });
            }
            */
        });
    });
