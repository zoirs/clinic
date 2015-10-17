'use strict';

angular.module('clinicApp')
    .controller('AreaController', function ($scope, Area, AreaSearch, ParseLinks) {
        $scope.areas = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Area.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.areas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
