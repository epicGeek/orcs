var systemUserControllers = angular.module('systemUserControllers', []);

systemUserControllers.controller('systemUserListCtrl', ["$scope","SystemUser",function($scope,SystemUser) {
	var restData = SystemUser.queryForPage(); // ;
	$scope.systemUserList = restData;
	$scope.orderProp = 'realName';
}]);
systemUserControllers.controller('systemUserDetailCtrl', ["$scope","$http","$routeParams",function($scope,$http,$routeParams) {
	var userDataLink = $routeParams.userDataLink;
	$http.get(userDataLink).success(function(data) {
		$scope.systemUser = data;
		console.log($scope.systemUser.systemRole);
	});

}]);