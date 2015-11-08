'use strict';

angular.module('clinicApp')
    .controller('DoctorController', function ($scope, Doctor, DoctorSearch, ParseLinks) {
        $scope.doctors = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Doctor.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.doctors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Doctor.get({id: id}, function(result) {
                $scope.doctor = result;
                $('#deleteDoctorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Doctor.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDoctorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            DoctorSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.doctors = result;
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
            $scope.doctor = {
                fio: null,
                alias: null,
                rating: null,
                ratingInternal: null,
                priceFirst: null,
                priceSpecial: null,
                sex: null,
                img: null,
                reviewCount: null,
                textAbout: null,
                experiencaYear: null,
                departure: null,
                category: null,
                degree: null,
                rank: null,
                extra: null,
                isActive: null,
                docdocId: null,
                lastUpdated: null,
                id: null
            };
        };
    });
