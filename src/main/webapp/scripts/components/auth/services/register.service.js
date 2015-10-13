'use strict';

angular.module('clinicApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


