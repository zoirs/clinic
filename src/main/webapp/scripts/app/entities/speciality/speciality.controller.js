'use strict';

angular.module('clinicApp')
    .controller('SpecialityController', function ($scope, Speciality, SpecialitySearch) {
        $scope.specialitys = [];
        $scope.loadAll = function() {
            Speciality.query(function(result) {
               $scope.specialitys = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Speciality.get({id: id}, function(result) {
                $scope.speciality = result;
                $('#deleteSpecialityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Speciality.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSpecialityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SpecialitySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.specialitys = result;
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
            $scope.speciality = {
                name: null,
                alias: null,
                nameGenitive: null,
                namePlural: null,
                namePluralGenitive: null,
                docdocId: null,
                lastUpdated: null,
                id: null
            };
        };
    });
