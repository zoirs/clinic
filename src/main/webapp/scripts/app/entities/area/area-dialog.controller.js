'use strict';

angular.module('clinicApp').controller('AreaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Area',
        function($scope, $stateParams, $modalInstance, entity, Area) {

        $scope.area = entity;
        $scope.load = function(id) {
            Area.get({id : id}, function(result) {
                $scope.area = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:areaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.area.id != null) {
                Area.update($scope.area, onSaveFinished);
            } else {
                Area.save($scope.area, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
