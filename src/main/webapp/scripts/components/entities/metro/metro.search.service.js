'use strict';

angular.module('clinicApp')
    .factory('MetroSearch', function ($resource) {
        return $resource('api/_search/metros/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
