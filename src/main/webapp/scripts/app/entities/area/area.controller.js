'use strict';

angular.module('clinicApp')
    .controller('AreaController', function ($scope, Area, AreaSearch) {
        $scope.areas = [];
        $scope.loadAll = function() {
            Area.query(function(result) {
               $scope.areas = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Area.get({id: id}, function(result) {
                $scope.area = result;
                $('#deleteAreaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Area.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAreaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            AreaSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.areas = result;
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
            $scope.area = {
                name: null,
                alias: null,
                docdocId: null,
                lastUpdated: null,
                id: null
            };
        };
    });
