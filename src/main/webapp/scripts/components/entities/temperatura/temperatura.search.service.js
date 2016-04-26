'use strict';

angular.module('nowLocateApp')
    .factory('TemperaturaSearch', function ($resource) {
        return $resource('api/_search/temperaturas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
