'use strict';

angular.module('nowLocateApp')
    .factory('ExpedicionSearch', function ($resource) {
        return $resource('api/_search/expedicions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
