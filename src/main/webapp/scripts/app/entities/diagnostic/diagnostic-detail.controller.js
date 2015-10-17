'use strict';

angular.module('clinicApp')
    .controller('DiagnosticDetailController', function ($scope, $rootScope, $stateParams, entity, Diagnostic) {
        $scope.diagnostic = entity;
        $scope.load = function (id) {
            Diagnostic.get({id: id}, function(result) {
                $scope.diagnostic = result;
            });
        };
        $rootScope.$on('clinicApp:diagnosticUpdate', function(event, result) {
            $scope.diagnostic = result;
        });
    });
