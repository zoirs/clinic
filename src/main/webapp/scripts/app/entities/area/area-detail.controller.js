'use strict';

angular.module('clinicApp')
    .controller('AreaDetailController', function ($scope, $rootScope, $stateParams, entity, Area) {
        $scope.area = entity;
        $scope.load = function (id) {
            Area.get({id: id}, function(result) {
                $scope.area = result;
            });
        };
        $rootScope.$on('clinicApp:areaUpdate', function(event, result) {
            $scope.area = result;
        });
    });
