'use strict';

angular.module('nowLocateApp')
    .factory('DelegacionSearch', function ($resource) {
        return $resource('api/_search/delegacions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
