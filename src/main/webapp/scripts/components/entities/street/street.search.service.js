'use strict';

angular.module('clinicApp')
    .factory('StreetSearch', function ($resource) {
        return $resource('api/_search/streets/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
