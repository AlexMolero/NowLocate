'use strict';

angular.module('nowLocateApp')
    .controller('DelegacionController', function ($scope, $state, Delegacion, DelegacionSearch, ParseLinks) {

        $scope.delegacions = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Delegacion.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.delegacions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            DelegacionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.delegacions = result;
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
            $scope.delegacion = {
                localidad: null,
                volumenAlmacen: null,
                calle: null,
                latitud: null,
                longitud: null,
                id: null
            };
        };
    });
