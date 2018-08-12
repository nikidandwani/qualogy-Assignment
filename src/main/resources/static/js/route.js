/**
 * 
 */
'use strict';

app.
config(['$routeProvider', function($routeProvider) {
   $routeProvider.when('/nicecards', {templateUrl: 'modules/tasklist/views/tasklist.html', controller: 'TodoController'});
   $routeProvider.when('/oldtodo', {templateUrl: 'modules/transaction/views/transaction.html', controller: 'getTransaction'});
   $routeProvider.otherwise({redirectTo: '/nicecards'});
}]);

