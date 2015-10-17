'use strict';

angular.module('clinicApp').controller('SpecialityDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Speciality',
        function($scope, $stateParams, $modalInstance, entity, Speciality) {

        $scope.speciality = entity;
        $scope.load = function(id) {
            Speciality.get({id : id}, function(result) {
                $scope.speciality = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:specialityUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.speciality.id != null) {
                Speciality.update($scope.speciality, onSaveFinished);
            } else {
                Speciality.save($scope.speciality, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
