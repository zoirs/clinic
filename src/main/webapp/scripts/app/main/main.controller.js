'use strict';

angular.module('clinicApp')
    .controller('MainController', function ($scope, $state, Principal, Metro, Speciality) {
        console.log('==================================');
        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.loadAllMetros = function () {
            Metro.query(function (result) {
                $scope.metros = result;
            });
        };

        $scope.specialitys = [];
        $scope.loadAllSpecialitys = function () {
            Speciality.query(function (result) {
                $scope.specialitys = result;
            });
        };

        $scope.params = {
            type: 'doctors',
            metro: null,
            speciality: null
        };

        $scope.find = function () {
            console.log($scope.params);
            $state.go('find', $scope.params);
        };


        $scope.loadAllMetros();

        $scope.loadAllSpecialitys();

    });
