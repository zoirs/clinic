'use strict';

angular.module('clinicApp')
    .factory('DiagnosticSearch', function ($resource) {
        return $resource('api/_search/diagnostics/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
