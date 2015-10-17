'use strict';

angular.module('clinicApp').controller('DiagnosticDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Diagnostic',
        function($scope, $stateParams, $modalInstance, entity, Diagnostic) {

        $scope.diagnostic = entity;
        $scope.diagnostics = Diagnostic.query();
        $scope.load = function(id) {
            Diagnostic.get({id : id}, function(result) {
                $scope.diagnostic = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('clinicApp:diagnosticUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.diagnostic.id != null) {
                Diagnostic.update($scope.diagnostic, onSaveFinished);
            } else {
                Diagnostic.save($scope.diagnostic, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
