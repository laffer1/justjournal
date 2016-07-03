angular.module('wwwApp').controller('TagsCtrl', ['$scope', 'TagService', function ($scope, TagService) {
  'use strict';

    $scope.tags = TagService.query({}, function() {
        $scope.sizeTags();
    });

    $scope.sizeTags = function() {
        var largest = 0;
        var smallest = 0;

        for (var i = 0; i < $scope.tags.length; i++) {
            var tmpcount = $scope.tags[i].count;

            if (tmpcount > largest)
                largest = tmpcount;

             if (tmpcount < smallest)
                smallest = tmpcount;
        }

        var cutsmall = largest / 3;
        var cutlarge = cutsmall * 2;

        for (var x = 0; x < $scope.tags.length; x++) {
             var count = $scope.tags[x].count;
             if (count > cutlarge) {
                 $scope.tags[x].Class = 'TagCloudLarge';
             } else if (count < cutsmall) {
                 $scope.tags[x].Class = 'TagCloudSmall';
             } else {
                 $scope.tags[x].Class = 'TagCloudMedium';
             }
        }
    };


}]);