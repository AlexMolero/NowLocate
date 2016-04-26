'use strict';

angular.module('nowLocateApp')
    .factory('Camion', function ($resource, DateUtils) {
        return $resource('api/camions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
