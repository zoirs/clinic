'use strict';

angular.module('clinicApp')
    .controller('DiagnosticController', function ($scope, Diagnostic, DiagnosticSearch) {
        $scope.diagnostics = [];
        $scope.loadAll = function() {
            Diagnostic.query(function(result) {
               $scope.diagnostics = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Diagnostic.get({id: id}, function(result) {
                $scope.diagnostic = result;
                $('#deleteDiagnosticConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Diagnostic.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDiagnosticConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            DiagnosticSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.diagnostics = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.diagnostic = {
                name: null,
                alias: null,
                docdocId: null,
                lastUpdated: null,
                id: null
            };
        };
    });
