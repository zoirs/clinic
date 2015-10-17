'use strict';

angular.module('clinicApp')
    .controller('StreetDetailController', function ($scope, $rootScope, $stateParams, entity, Street, City) {
        $scope.street = entity;
        $scope.load = function (id) {
            Street.get({id: id}, function(result) {
                $scope.street = result;
            });
        };
        $rootScope.$on('clinicApp:streetUpdate', function(event, result) {
            $scope.street = result;
        });
    });
