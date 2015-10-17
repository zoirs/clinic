'use strict';

angular.module('clinicApp')
    .factory('SpecialitySearch', function ($resource) {
        return $resource('api/_search/specialitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
