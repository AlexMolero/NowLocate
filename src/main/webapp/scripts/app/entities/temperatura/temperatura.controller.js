'use strict';

angular.module('nowLocateApp')
    .controller('TemperaturaController', function ($scope, $state, Temperatura, TemperaturaSearch, ParseLinks) {

        $scope.temperaturas = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Temperatura.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.temperaturas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TemperaturaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.temperaturas = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.temperatura = {
                hora: null,
                temperatura: null,
                id: null
            };
        };
    });
