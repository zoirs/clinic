'use strict';

angular.module('clinicApp').controller('StreetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Street', 'City',
        function($scope, $stateParams, $modalInstance, entity, Street, City) {

        $scope.street = entity;
        $scope.citys = City.query();
        $scope.load = function(id) {
            Street.get({id : id}, function(result) {
                $scope.street = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:streetUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.street.id != null) {
                Street.update($scope.street, onSaveFinished);
            } else {
                Street.save($scope.street, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
