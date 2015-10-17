'use strict';

angular.module('clinicApp')
    .factory('City', function ($resource, DateUtils) {
        return $resource('api/citys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastUpdated = DateUtils.convertDateTimeFromServer(data.lastUpdated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
