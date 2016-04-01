'use strict';

angular.module('clinicApp')
    .controller('SpecListController', function ($scope, $state, Principal, Speciality) {

        console.log('================SpecListController==================');

        $scope.specialitys = [];
        $scope.loadAllSpecialitys = function () {
            Speciality.query(function (result) {
                var countInRow = Math.ceil(result.length / 3);
                $scope.specialitys[0] = result.splice(0,countInRow) ;
                $scope.specialitys[1] = result.splice(0,countInRow) ;
                $scope.specialitys[2] = result.splice(0,countInRow) ;
            });
        };

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.loadAllSpecialitys();

    });
