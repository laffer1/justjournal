/*
 * Copyright (c) 2013 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

angular.module('wwwApp').controller('TagsCtrl', ['$scope', 'TagService', function ($scope, TagService) {
  'use strict';

    $scope.tags = TagService.query();

    $scope.sizeTags = function() {
        var largest = 0;
        var smallest = 0;

        for (var i = 0; i < $scope.tags.length; i++) {
            var tmpcount = $scope.tags[i].Count;

            if (tmpcount > largest)
                largest = tmpcount;

             if (tmpcount < smallest)
                smallest = tmpcount;
        }

        var cutsmall = largest / 3;
        var cutlarge = cutsmall * 2;

        for (var x = 0; x < $scope.tags.length; x++) {
             var count = $scope.tags[x].Count;
             if (count > cutlarge) {
                 $scope.tags[x].Class = 'TagCloudLarge';
             } else if (count < cutsmall) {
                 $scope.tags[x].Class = 'TagCloudSmall';
             } else {
                 $scope.tags[x].Class = 'TagCloudMedium';
             }
        }
    };

    $scope.sizeTags();
}]);