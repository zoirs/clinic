'use strict';

angular.module('clinicApp')
    .controller('StreetController', function ($scope, Street, StreetSearch, ParseLinks) {
        $scope.streets = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Street.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.streets = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Street.get({id: id}, function(result) {
                $scope.street = result;
                $('#deleteStreetConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Street.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStreetConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            StreetSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.streets = result;
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
            $scope.street = {
                name: null,
                alias: null,
                docdocId: null,
                lastUpdate: null,
                id: null
            };
        };
    });
