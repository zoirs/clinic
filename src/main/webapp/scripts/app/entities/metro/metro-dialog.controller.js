'use strict';

angular.module('clinicApp').controller('MetroDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Metro', 'City',
        function($scope, $stateParams, $modalInstance, entity, Metro, City) {

        $scope.metro = entity;
        $scope.citys = City.query();
        $scope.load = function(id) {
            Metro.get({id : id}, function(result) {
                $scope.metro = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:metroUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.metro.id != null) {
                Metro.update($scope.metro, onSaveFinished);
            } else {
                Metro.save($scope.metro, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
