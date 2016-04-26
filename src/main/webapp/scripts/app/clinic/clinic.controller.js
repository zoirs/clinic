'use strict';

angular.module('clinicApp')
    .controller('ClinicShowController', function ($scope, $state, entity, Clinic) {

        console.log('================ ClinicController ==================');

        $scope.clinic = entity;

        $scope.load = function (id) {
            Clinic.get({id: id}, function(result) {
                $scope.clinic = result;
            });
        };



    });
