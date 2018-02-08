var fusionApp = angular.module('fusionApp', [ "ngMaterial","ngRoute","systemUserControllers","systemUserServices"]);




fusionApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/system-user', {
		templateUrl : 'system/user-list.html',
		controller : 'systemUserListCtrl'
	}).when('/system-user/:userDataLink', {
		templateUrl : 'system/user-detail.html',
		controller : 'systemUserDetailCtrl'
	}).otherwise({
		redirectTo : '/system-user'
	});
} ]);
fusionApp.controller('AppCtrl', ['$scope', '$mdSidenav', function($scope, $mdSidenav){
	  $scope.toggleSidenav = function(menuId) {
	    $mdSidenav(menuId).toggle();
	  };
}]);
fusionApp.controller('AppCtrl', function($scope, $http) {
	$http.get('system-user/my-profile').success(function(data) {
		$scope.greeting = data;
	}).error(function(data) {
		window.location = "welcome";
	});
});