'use strict';

angular.module('nowLocateApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


