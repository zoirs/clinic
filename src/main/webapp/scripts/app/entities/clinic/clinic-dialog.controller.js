'use strict';

angular.module('clinicApp').controller('ClinicDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Clinic', 'City', 'Street', 'Area', 'Diagnostic', 'Metro', 'Speciality',
        function($scope, $stateParams, $modalInstance, entity, Clinic, City, Street, Area, Diagnostic, Metro, Speciality) {

        $scope.clinic = entity;
        $scope.citys = City.query();
        $scope.streets = Street.query();
        $scope.areas = Area.query();
        $scope.diagnostics = Diagnostic.query();
        $scope.metros = Metro.query();
        $scope.specialitys = Speciality.query();
        $scope.load = function(id) {
            Clinic.get({id : id}, function(result) {
                $scope.clinic = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:clinicUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.clinic.id != null) {
                Clinic.update($scope.clinic, onSaveFinished);
            } else {
                Clinic.save($scope.clinic, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
