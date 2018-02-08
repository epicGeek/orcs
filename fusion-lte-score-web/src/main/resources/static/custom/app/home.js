var systemUserControllers = angular.module('systemUserControllers', []);

systemUserControllers.controller('greetingCtrl', function($scope, $http) {
	$http.get('system-user/my-profile').success(function(data) {
		$scope.greeting = data;
	}).error(function(data) {
		window.location = "login";
	});
});

systemUserControllers.controller('systemUserListCtrl', function($scope, $http) {
	$http.get('rest/system-user').success(function(data) {
		$scope.systemUserList = data["_embedded"]["system-user"];
	});
	$scope.showDetail = function(obj) {
		alert(obj.systemRole.length);
	},
	$scope.orderProp = 'realName';
});
systemUserControllers.controller('systemUserDetailCtrl', function($scope,$http,$routeParams) {
	var userDataLink = $routeParams.userDataLink;
	$http.get(userDataLink).success(function(data){
		$scope.systemUser = data;
		console.log($scope.systemUser.systemRole);
	});
	
});