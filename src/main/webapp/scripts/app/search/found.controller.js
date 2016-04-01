'use strict';

angular.module('clinicApp')
    .controller('FoundController', function ($scope, $state, Principal, ParseLinks, $stateParams, Find) {

        console.log('================FoundController==================');
        console.log('$stateParams', $stateParams);

        $scope.doctors = [];
        $scope.clinics = [];

        $scope.page = 0;

        $scope.loadAll = function () {
            $scope.$parent.params = angular.copy($state.params);

            Find.query({
                page: $scope.page, size: 10,
                type: $state.params.type,
                metro: $state.params.metro,
                speciality: $state.params.speciality
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                if ($scope.params.type == 'doctors') {
                    $scope.clinics = [];
                    $scope.doctors = result;
                }
                if ($scope.params.type == 'clinics') {
                    $scope.doctors = [];
                    $scope.clinics = result;
                }
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        //$scope.found = found;
        //$scope.links = ParseLinks.parse(headers('link'));


    });
