'use strict';

angular.module('clinicApp')
    .controller('MetroController', function ($scope, Metro, MetroSearch) {
        $scope.metros = [];
        $scope.loadAll = function() {
            Metro.query(function(result) {
               $scope.metros = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Metro.get({id: id}, function(result) {
                $scope.metro = result;
                $('#deleteMetroConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Metro.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMetroConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MetroSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.metros = result;
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
            $scope.metro = {
                name: null,
                alias: null,
                lineName: null,
                lineColor: null,
                docdocId: null,
                lastUpdated: null,
                id: null
            };
        };
    });
