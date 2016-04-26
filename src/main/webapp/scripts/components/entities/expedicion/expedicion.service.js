'use strict';

angular.module('nowLocateApp')
    .factory('Expedicion', function ($resource, DateUtils) {
        return $resource('api/expedicions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fechaInicio = DateUtils.convertLocaleDateFromServer(data.fechaInicio);
                    data.fechaEntrega = DateUtils.convertLocaleDateFromServer(data.fechaEntrega);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.fechaInicio = DateUtils.convertLocaleDateToServer(data.fechaInicio);
                    data.fechaEntrega = DateUtils.convertLocaleDateToServer(data.fechaEntrega);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.fechaInicio = DateUtils.convertLocaleDateToServer(data.fechaInicio);
                    data.fechaEntrega = DateUtils.convertLocaleDateToServer(data.fechaEntrega);
                    return angular.toJson(data);
                }
            }
        });
    });
