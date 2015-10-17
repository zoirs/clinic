'use strict';

angular.module('clinicApp')
    .factory('AreaSearch', function ($resource) {
        return $resource('api/_search/areas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
