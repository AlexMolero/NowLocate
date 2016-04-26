'use strict';

angular.module('nowLocateApp')
    .factory('Temperatura', function ($resource, DateUtils) {
        return $resource('api/temperaturas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.hora = DateUtils.convertDateTimeFromServer(data.hora);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
