angular.module('wwwApp').controller('MainCtrl', ['$scope', '$http', function ($scope, $http) {
    'use strict';

    $scope.username = '';
    $scope.password = '';

    $scope.AccountLogin = function() {
        if (typeof $scope.username === 'undefined' || $scope.username.length < 3)
            alert('Username must be greater than 2 characters');

        if (typeof $scope.password === 'undefined' || $scope.password.length < 5)
            alert('Password must be greater than 4 characters');

        var data = { username: $scope.username, password: $scope.password };
        $http.post('api/login', data).success(function(data, status) {
              if (status == 200 && data.status == 'JJ.LOGIN.OK') {
                  window.location.href = '/users/' + data.username;
                  return false;
              } else {
                  alert("Your login information was invalid. Please try again");
                  return false;
              }
        }).fail(function() {
              alert("Your login information was invalid. Please try again");
              return false;
        });
    };

    $scope.CreateAccount = function() {
        var data = $scope.create;
        $http.post('api/login?email=' + $scope.email, data).success(function(data,status) {
            alert('Your account has been created. You may login after responding to the verification email.');
            return false;
        }.fail(function() {
            alert('Unable to create this account. Please verify all fields and try again');
            return false;
        }));
    };
}]);
