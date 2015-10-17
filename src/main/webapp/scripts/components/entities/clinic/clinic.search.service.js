'use strict';

angular.module('clinicApp')
    .factory('ClinicSearch', function ($resource) {
        return $resource('api/_search/clinics/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
