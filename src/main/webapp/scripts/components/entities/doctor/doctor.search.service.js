'use strict';

angular.module('clinicApp')
    .factory('DoctorSearch', function ($resource) {
        return $resource('api/_search/doctors/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
