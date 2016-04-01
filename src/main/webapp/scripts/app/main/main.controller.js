'use strict';

angular.module('clinicApp')
    .controller('MainController', function ($scope, $state, $stateParams, Principal, Metro, Speciality) {
        console.log('==============MainController====================');
        console.log('$state.params', $state.params);
        console.log('$stateParams', $stateParams);

        //$scope.params = $state.params;
        $scope.params = angular.copy($state.params);

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
        $scope.metros = [];
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

        $scope.loadAllMetros();

        $scope.loadAllSpecialitys();

        if (!$scope.params.metro) {
            $scope.params.metro = 'all';
        }
        if (!$scope.params.speciality) {
            $scope.params.speciality = 'all';
        }
        if (!$scope.params.type) {
            $scope.params.type = 'doctors';
        }
    });
