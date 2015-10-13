 'use strict';

angular.module('clinicApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-clinicApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-clinicApp-params')});
                }
                return response;
            }
        };
    });
