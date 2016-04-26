'use strict';

angular.module('nowLocateApp')
    .factory('CamionSearch', function ($resource) {
        return $resource('api/_search/camions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
