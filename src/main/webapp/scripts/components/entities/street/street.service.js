'use strict';

angular.module('clinicApp')
    .factory('Street', function ($resource, DateUtils) {
        return $resource('api/streets/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastUpdate = DateUtils.convertDateTimeFromServer(data.lastUpdate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
