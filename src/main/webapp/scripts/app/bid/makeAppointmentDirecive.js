'use strict';

var module = angular.module('clinicApp');

module
    .directive('makeAppointmenDialog', function () {
        return {
            restrict: 'E',
            scope: {
                specialitys: '=specialitys',
                show: '=show',
                clinicid: '@clinicid',
                doctorid: '@doctorid',
                name: '@name',
                type: '@type'
            },
            replace: true, // Replace with the template below
            //transclude: true, // we want to insert custom content inside the directive
            link: function (scope, element, attrs) {
                scope.hideModal = function () {
                    scope.show = false;
                };
            },
            controller: function ($scope, $http, Speciality) {

                $scope.createAppointmen = true;

                $scope.loadAllSpecialitys = function () {
                    Speciality.query(function (result) {
                        $scope.specialitys = result;
                    });
                };
                $scope.sendRequest = function () {
                    console.log($scope.appointmen);

                    $http.post('/api/createappointmen/', $scope.appointmen)
                        .then(function (response) {
                            $scope.createAppointmen = false;

                            //console.log(response);
                        });
                }
            },
            templateUrl: "scripts/app/bid/appointmentDialog.html"
        };
    }).directive('makeAppointmen', function () {
        return {
            restrict: 'E',
            scope: {
                specialitys: '=specialitys',
                clinicid: '@clinicid',
                doctorid: '@doctorid',
                name: '@name',
                type: '@type'
            },
            replace: true, // Replace with the template below
            transclude: true, // we want to insert custom content inside the directive
            link: function (scope, element, attrs) {
            },
            controller: function ($scope) {
                console.log("===1");
                $scope.modalShown = false;
                $scope.toggleModal = function () {
                    $scope.modalShown = !$scope.modalShown;
                };
            },
            templateUrl: "scripts/app/bid/appointmentButton.html"
        };
    });

;
