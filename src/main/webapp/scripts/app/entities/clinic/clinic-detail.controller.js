'use strict';

angular.module('clinicApp')
    .controller('ClinicDetailController', function ($scope, $rootScope, $stateParams, entity, Clinic, City, Street, Area, Diagnostic, Metro, Speciality) {
        $scope.clinic = entity;
        $scope.load = function (id) {
            Clinic.get({id: id}, function(result) {
                $scope.clinic = result;
            });
        };
        $rootScope.$on('clinicApp:clinicUpdate', function(event, result) {
            $scope.clinic = result;
        });
    });
