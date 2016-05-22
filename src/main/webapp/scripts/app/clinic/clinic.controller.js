'use strict';

angular.module('clinicApp')
    .controller('ClinicShowController', function ($scope, $state, entity, Clinic) {

        console.log('================ ClinicController ==================');

        entity.$promise.then(function (clinic) {
            $scope.clinic = clinic;
        });

    });
