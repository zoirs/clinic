'use strict';

angular.module('clinicApp')
    .controller('SpecialityDetailController', function ($scope, $rootScope, $stateParams, entity, Speciality) {
        $scope.speciality = entity;
        $scope.load = function (id) {
            Speciality.get({id: id}, function(result) {
                $scope.speciality = result;
            });
        };
        $rootScope.$on('clinicApp:specialityUpdate', function(event, result) {
            $scope.speciality = result;
        });
    });
