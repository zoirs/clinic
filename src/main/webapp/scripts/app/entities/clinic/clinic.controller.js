'use strict';

angular.module('clinicApp')
    .controller('ClinicController', function ($scope, Clinic, ClinicSearch, ParseLinks) {
        $scope.clinics = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Clinic.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.clinics = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Clinic.get({id: id}, function(result) {
                $scope.clinic = result;
                $('#deleteClinicConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Clinic.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteClinicConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ClinicSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.clinics = result;
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
            $scope.clinic = {
                name: null,
                shortName: null,
                alias: null,
                url: null,
                longitude: null,
                latitude: null,
                streetName: null,
                house: null,
                description: null,
                weekdaysOpen: null,
                weekendOpen: null,
                shortDescription: null,
                isDiagnostic: null,
                isClinic: null,
                isDoctor: null,
                phoneContact: null,
                phoneAppointmen: null,
                phoneReplacement: null,
                logoPath: null,
                logo: null,
                scheduleStateOnline: null,
                email: null,
                minPrice: null,
                maxPrice: null,
                docdocId: null,
                lastUpdated: null,
                id: null
            };
        };
    });
