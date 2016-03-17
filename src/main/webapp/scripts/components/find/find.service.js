'use strict';

angular.module('clinicApp')
    .factory('Find', function ($resource, DateUtils) {
        return $resource('api/find/:type/:metro/:speciality', {}, {
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
