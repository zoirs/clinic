'use strict';

angular.module('clinicApp').controller('DoctorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Doctor', 'Clinic', 'Speciality', 'Metro', 'City',
        function($scope, $stateParams, $modalInstance, entity, Doctor, Clinic, Speciality, Metro, City) {

        $scope.doctor = entity;
        $scope.clinics = Clinic.query();
        $scope.specialitys = Speciality.query();
        $scope.metros = Metro.query();
        $scope.citys = City.query();
        $scope.load = function(id) {
            Doctor.get({id : id}, function(result) {
                $scope.doctor = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:doctorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.doctor.id != null) {
                Doctor.update($scope.doctor, onSaveFinished);
            } else {
                Doctor.save($scope.doctor, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
