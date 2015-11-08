'use strict';

angular.module('clinicApp')
    .controller('DoctorDetailController', function ($scope, $rootScope, $stateParams, entity, Doctor, Clinic, Speciality, Metro, City) {
        $scope.doctor = entity;
        $scope.load = function (id) {
            Doctor.get({id: id}, function(result) {
                $scope.doctor = result;
            });
        };
        $rootScope.$on('clinicApp:doctorUpdate', function(event, result) {
            $scope.doctor = result;
        });
    });
