angular.module('wwwApp').controller('MainCtrl', ['$scope', '$http', function ($scope, $http) {
    'use strict';

    $scope.username = '';
    $scope.password = '';

    $scope.AccountLogin = function() {
        var data = { username: $scope.username, password: $scope.password };
        $http.post('api/login', data).success(function(data, status) {
              if (status == 200 && data.status == 'JJ.LOGIN.OK') {
                  window.location.href = '/users/' + data.username;
              } else {
                  alert("Your login information was invalid. Please try again");
              }
        }).fail(function() {
                    alert("Your login information was invalid. Please try again");
        });
    };
}]);
