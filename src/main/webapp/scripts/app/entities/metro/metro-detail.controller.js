'use strict';

angular.module('clinicApp')
    .controller('MetroDetailController', function ($scope, $rootScope, $stateParams, entity, Metro, City) {
        $scope.metro = entity;
        $scope.load = function (id) {
            Metro.get({id: id}, function(result) {
                $scope.metro = result;
            });
        };
        $rootScope.$on('clinicApp:metroUpdate', function(event, result) {
            $scope.metro = result;
        });
    });
