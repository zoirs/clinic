'use strict';

angular.module('clinicApp')
    .controller('FoundController', function ($scope, $state, Principal, ParseLinks, $stateParams, Find) {

        console.log('================FoundController==================');

        $scope.found = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            Find.query({
                page: $scope.page, size: 20,
                type: $stateParams.type,
                metro: $stateParams.metro,
                speciality: $stateParams.speciality
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.found = result;
            });
        };

        $scope.loadPage = function(page) {
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
