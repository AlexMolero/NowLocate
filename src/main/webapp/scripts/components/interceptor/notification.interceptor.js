 'use strict';

angular.module('nowLocateApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-nowLocateApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-nowLocateApp-params')});
                }
                return response;
            }
        };
    });
