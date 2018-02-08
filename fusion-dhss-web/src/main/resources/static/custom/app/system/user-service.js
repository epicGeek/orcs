var systemUserServices = angular.module('systemUserServices', [ 'ngResource' ]);

systemUserServices.factory('SystemUser', [ '$resource', function($resource) {
	return $resource('rest/system-user/:userId', {userId:'@id'}, {
		queryForPage : {
			method : 'GET',
			params : {},
			isArray : true,
			transformResponse:function(data,headersGetter){
				var jsonData = angular.fromJson(data);
				var systemUserList = [];
				console.log(jsonData);
				if(jsonData["_embedded"]){
					angular.forEach(jsonData["_embedded"]["system-user"], function(item){ 
			            systemUserList.push(item);
			        });
				}
				return systemUserList;
			}
		}
	});
} ]);
