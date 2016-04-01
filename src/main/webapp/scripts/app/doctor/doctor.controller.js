'use strict';

angular.module('clinicApp')
    .controller('DoctorShowController', function ($scope, $state, entity, Doctor) {

        console.log('================ DoctorController ==================');

        entity.$promise.then(function (doctor) {
            $scope.doctor = doctor;
            var q = $scope.doctor.img;
            $scope.doctor.imgFull = q.substring(0, q.indexOf('_small')) + q.substring(q.indexOf('_small') + 6);
        });


    });
